package com.example.mkwallet.data.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.mkwallet.data.models.GetExchangeRates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ExchangeRatesDao {

    @Upsert
    suspend fun upsertExchangeRates(exchangeRates: GetExchangeRates)

    @Delete
    suspend fun deleteExchangeRates(exchangeRates: GetExchangeRates)

    @Upsert
    suspend fun upsertAllExchangeRates(exchangeRates: List<GetExchangeRates>)

    @Query("SELECT * FROM exchange_rates ORDER BY valuta ASC")
    fun getExchangeRatesOrderedById(): Flow<List<GetExchangeRates>>

    @Query("SELECT * FROM exchange_rates WHERE valuta=:valuta")
    fun getExchangeRatesById(valuta: Double): Flow<GetExchangeRates?>

    @Query("SELECT sreden FROM EXCHANGE_RATES WHERE oznaka=:oznaka ")
    fun getSredenByOznaka(oznaka: String): LiveData<Double>

    @Query("SELECT oznaka FROM exchange_rates ORDER BY oznaka ASC")
    fun getOznakiAsc() : LiveData<List<String>>

    fun getExchangeRatesByIdDistinctUntilChanged(id: Double) = getExchangeRatesById(id).distinctUntilChanged()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exchangeRates: List<GetExchangeRates>)
}