package com.example.mkwallet.ui.exchangeRates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mkwallet.data.models.GetExchangeRates
import com.example.mkwallet.databinding.ExchangeRatesItemBinding
import java.util.Locale

class ExchangeRatesAdapter : ListAdapter<GetExchangeRates, ExchangeRatesAdapter.ExchangeRatesViewHolder>(ExchangeRatesListComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRatesViewHolder {
        val binding =
            ExchangeRatesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeRatesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeRatesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    // View Holder class to hold the view
    class ExchangeRatesViewHolder(private val binding: ExchangeRatesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exchangeRates: GetExchangeRates) {
            binding.apply {
                exchangeRatesName.text = exchangeRates.nazivMak
                exchangeRatesCountry.text = exchangeRates.drzava
                exchangeRatesCurrency.text = exchangeRates.oznaka
                exchangeRatesSold.text = String.format(Locale.GERMAN, format = "%.3f", exchangeRates.prodazen)
                exchangeRatesAverage.text = String.format(Locale.GERMAN, format = "%.3f", exchangeRates.sreden)
                exchangeRatesBought.text = String.format(Locale.GERMAN, format = "%.3f", exchangeRates.kupoven)
            }
        }
    }

    // Comparator class to check for the changes made.
    // If there are no changes then no need to do anything.
    class ExchangeRatesListComparator : DiffUtil.ItemCallback<GetExchangeRates>() {
        override fun areItemsTheSame(oldItem: GetExchangeRates, newItem: GetExchangeRates) =
            oldItem.valuta == newItem.valuta

        override fun areContentsTheSame(oldItem: GetExchangeRates, newItem: GetExchangeRates) =
            oldItem == newItem
    }
}