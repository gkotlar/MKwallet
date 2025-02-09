package com.example.mkwallet.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mkwallet.R
import com.example.mkwallet.databinding.FragmentHomeBinding
import com.example.mkwallet.services.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var currencySpinner: Spinner
    private lateinit var currencyEditText: EditText
    private lateinit var submitBtn: Button
    private lateinit var mWorkManager: WorkManager
    private lateinit var workRequest: PeriodicWorkRequest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this@HomeFragment.requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        mWorkManager = WorkManager.getInstance(requireActivity())
        currencySpinner = view.findViewById(R.id.currency_spinner)
        submitBtn = view.findViewById(R.id.submit_btn)
        currencyEditText = view.findViewById(R.id.valueEditText)

        val currencySpinnerObserver = Observer<List<String>> { result ->
            populateSpinner(currencySpinner, result, homeViewModel)
        }
        homeViewModel.oznaki.observe(this.requireActivity(), currencySpinnerObserver)

        submitBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                homeViewModel.setNumber(currencyEditText.text.toString())
                val inputData: Data = Data.Builder()
                    .putString("run_type", "background")
                    .putDouble("rate", homeViewModel.number)
                    .putString("oznaka", homeViewModel.oznaka.value)
                    .build()

                var constraints : Constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .setRequiresBatteryNotLow(true)
                    .build()

                workRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 12, TimeUnit.HOURS)
                    .setInputData(inputData)
                    .setConstraints(constraints)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                        TimeUnit.MILLISECONDS)
                    .build()
                mWorkManager.enqueueUniquePeriodicWork(
                    "background_work",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    workRequest
                )
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun populateSpinner(spinner: Spinner, currencyList: List<String>, homeViewModel: HomeViewModel) {
        // Sort the currency list alphabetically and populate the spinner
        val adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        homeViewModel.oznaka.value = spinner.getItemAtPosition(0).toString()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    homeViewModel.oznaka.value = parent.getItemAtPosition(position).toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}