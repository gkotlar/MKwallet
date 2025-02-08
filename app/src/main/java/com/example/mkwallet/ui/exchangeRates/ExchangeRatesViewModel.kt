package com.example.mkwallet.ui.exchangeRates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mkwallet.data.ExchangeRatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExchangeRatesViewModel @Inject constructor( repository : ExchangeRatesRepository ) : ViewModel() {
    val formater = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private var localDate = LocalDate.now()
    val year = localDate.year
    val month = localDate.monthValue
    val day = localDate.dayOfMonth
    private var exchangeRateTo = 0.0
    private var exchangeRateFrom = 0.0

    val currentDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>(localDate.format(formater))
    }
    val currencyFrom: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currencyTo: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val valueTo: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    var exchangeRates = repository.getExchangeRates(startDate = currentDate.value.toString(), endDate = currentDate.value.toString()).asLiveData()
    var oznaki : LiveData<List<String>> = repository.getOznaki()

    fun setCurrentDate(year:Int, month:Int, day:Int){
        currentDate.value = LocalDate.of(year,month,day).format(formater)
    }

    suspend fun getConversionFrom(flag : Boolean){
        withContext(Dispatchers.Default){
            if (flag) {
                if (currencyFrom.value != null) {
                    exchangeRates.value?.data?.asFlow()?.filter { exchangeRates ->
                        exchangeRates.oznaka == currencyFrom.value
                    }?.collect { exchangeRates ->
                        exchangeRateFrom = exchangeRates.sreden
                    }
                }
            }else {
                if (currencyTo.value != null) {
                    exchangeRates.value?.data?.asFlow()?.filter { exchangeRates ->
                        exchangeRates.oznaka == currencyTo.value
                    }?.collect { exchangeRates ->
                        exchangeRateTo = exchangeRates.sreden
                    }
                }
            }
        }
    }


    fun setValue(num : String){
        if (num.toDoubleOrNull() != null){
            viewModelScope.launch {
                getConversionFrom(true)
                getConversionFrom(false)

                if(!exchangeRateTo.isNaN() && !exchangeRateFrom.isNaN()){
                    valueTo.value = num.toDouble() * exchangeRateFrom / exchangeRateTo
                }else{
                    valueTo.value = 0.0
                }
            }
        }
        else{
            valueTo.value = 0.0
        }
    }

    fun getVal(oznaka : String, repository : ExchangeRatesRepository) : LiveData<Double>{
        return repository.getSredenByOznaka(oznaka)
    }

    fun getExchangeRatesForDate(date : String, repository : ExchangeRatesRepository){
        exchangeRates = repository.getExchangeRates(startDate = currentDate.value.toString(), endDate = currentDate.value.toString()).asLiveData()
    }




}
