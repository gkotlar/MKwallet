package com.example.mkwallet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mkwallet.data.ExchangeRatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor( repository : ExchangeRatesRepository) : ViewModel() {

    var number : Double = 0.0
    var oznaki : LiveData<List<String>> = repository.getOznaki()
    val oznaka: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setNumber(num : String){
        if(num.toDoubleOrNull() != null){
            number = num.toDouble()
        }
    }


    private val _text = MutableLiveData<String>().apply {
        value = "Select the currency and enter the middle exchange rate limit where you want to be notified"
    }
    val text: LiveData<String> = _text
}