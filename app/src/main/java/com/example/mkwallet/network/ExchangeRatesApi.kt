package com.example.mkwallet.network

import com.example.mkwallet.data.models.GetExchangeRates
import retrofit2.http.GET


import retrofit2.http.Query



//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .baseUrl(BASE_URL)
//    .build()

interface ExchangeRatesApi {
    companion object{
        const val BASE_URL =
            "https://www.nbrm.mk/KLServiceNOV/"
    }
    @GET("GetExchangeRates")
    suspend fun getExchangeRatesList(
                         @Query(value="StartDate", encoded=true) startDate : String,
                         @Query(value = "EndDate", encoded=true) endDate : String,
                         @Query(value = "Format", encoded=true) format : String) : List<GetExchangeRates>
}