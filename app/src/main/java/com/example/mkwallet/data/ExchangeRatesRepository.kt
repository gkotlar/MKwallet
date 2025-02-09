package com.example.mkwallet.data

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.example.mkwallet.network.ExchangeRatesApi
import com.example.mkwallet.network.networkBoundResource
import javax.inject.Inject

class ExchangeRatesRepository @Inject constructor(
    private val api: ExchangeRatesApi,
    private val db: MyRoomDB
) {
    private val exchangeRatesDao = db.ExchangeRateDao()

    fun getExchangeRates(startDate: String, endDate: String) = networkBoundResource(
        query = {
            exchangeRatesDao.getExchangeRatesOrderedById()
        },
        fetch = {
            api.getExchangeRatesList(startDate = startDate, endDate = endDate, format = "json")
        },
        saveFetchResult = { exchangeRatesList ->
            db.withTransaction {
                exchangeRatesDao.insertAll(exchangeRatesList)
            }

        }
    )
    fun getOznaki() : LiveData<List<String>> {
        return exchangeRatesDao.getOznakiAsc()
    }
    fun getSredenByOznaka(oznaka:String) : LiveData<Double> {
        return exchangeRatesDao.getSredenByOznaka(oznaka)
    }

}