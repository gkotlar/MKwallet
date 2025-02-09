package com.example.mkwallet.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mkwallet.data.dao.AccountDao
import com.example.mkwallet.data.dao.ExchangeRatesDao
import com.example.mkwallet.data.dao.TransactionDao
import com.example.mkwallet.data.models.Account
import com.example.mkwallet.data.models.GetExchangeRates
import com.example.mkwallet.data.models.Transaction

@Database(entities = [Account::class, Transaction::class, GetExchangeRates::class], version = 1)
abstract class MyRoomDB : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun ExchangeRateDao(): ExchangeRatesDao
    }