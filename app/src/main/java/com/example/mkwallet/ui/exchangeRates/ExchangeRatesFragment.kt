package com.example.mkwallet.ui.exchangeRates

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mkwallet.R
import com.example.mkwallet.databinding.FragmentExchangeRatesBinding
import com.example.mkwallet.network.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ExchangeRatesFragment : Fragment() {

    private var _binding: FragmentExchangeRatesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var amountFromTextEdit: EditText
    private lateinit var amountToTextView: TextView
    private lateinit var dateTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangeRatesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exchangeRatesAdapter = ExchangeRatesAdapter()
        val exchangeRatesViewModel =
            ViewModelProvider(this)[ExchangeRatesViewModel::class.java]

        dateTextView = view.findViewById(R.id.date)
        fromCurrencySpinner = view.findViewById(R.id.spinner_from)
        toCurrencySpinner = view.findViewById(R.id.spinner_to)
        amountFromTextEdit = view.findViewById(R.id.ammount_from)
        amountToTextView = view.findViewById(R.id.ammount_to)

        val dateObserver = Observer<String> { newDate ->
            dateTextView.text = newDate
        }

        exchangeRatesViewModel.currentDate.observe(this.requireActivity(), dateObserver)

        val amountToObserver = Observer<Double> { result ->
            amountToTextView.text = String.format(Locale.GERMAN, "%.02f", result)
        }
        exchangeRatesViewModel.valueTo.observe(this.requireActivity(), amountToObserver)

        val toCurrencySpinnerObserver = Observer<List<String>> { result ->
            populateSpinner(toCurrencySpinner, result, exchangeRatesViewModel , false)
        }
        exchangeRatesViewModel.oznaki.observe(this.requireActivity(), toCurrencySpinnerObserver)

        val fromCurrencySpinnerObserver = Observer<List<String>> { result ->
            populateSpinner(fromCurrencySpinner, result, exchangeRatesViewModel, true)
        }
        exchangeRatesViewModel.oznaki.observe(this.requireActivity(), fromCurrencySpinnerObserver)

        view.apply{
            binding.apply {
                recyclerViewer.apply {
                    adapter = exchangeRatesAdapter
                    layoutManager = LinearLayoutManager(this@ExchangeRatesFragment.requireActivity())
                }
                exchangeRatesViewModel.exchangeRates.observe(this@ExchangeRatesFragment.requireActivity()){result->
                    exchangeRatesAdapter.submitList(result.data)
                    progressBar.isVisible = result is Resource.Loading<*> && result.data.isNullOrEmpty()
                    textViewError.isVisible = result is Resource.Error<*> && result.data.isNullOrEmpty()
                    textViewError.text = result.error?.localizedMessage
                }
            }
        }

        amountFromTextEdit.setOnKeyListener(OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                exchangeRatesViewModel.setValue(amountFromTextEdit.text.toString())
                return@OnKeyListener true
            }
            false
        })

        dateTextView.setOnClickListener{
            val year = exchangeRatesViewModel.year
            val month = exchangeRatesViewModel.month
            val day = exchangeRatesViewModel.day
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { view, yearC, monthOfYear, dayOfMonth ->
                    exchangeRatesViewModel.setCurrentDate(yearC, (monthOfYear+1), dayOfMonth)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun populateSpinner(spinner: Spinner, currencyList: List<String>, exchangeRatesViewModel : ExchangeRatesViewModel, type : Boolean /*false for to, true for from*/) {
        // Sort the currency list alphabetically and populate the spinner
        val adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        if(type){
            exchangeRatesViewModel.currencyFrom.value = spinner.getItemAtPosition(0).toString()
        }else{
            exchangeRatesViewModel.currencyTo.value = spinner.getItemAtPosition(0).toString()
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    if(type){
                        exchangeRatesViewModel.currencyFrom.value = parent.getItemAtPosition(position).toString()
                    }else{
                        exchangeRatesViewModel.currencyTo.value = parent.getItemAtPosition(position).toString()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}

