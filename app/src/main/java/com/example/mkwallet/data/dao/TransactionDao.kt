package com.example.mkwallet.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.mkwallet.data.models.Account
import com.example.mkwallet.data.models.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction` ORDER BY transaction_id DESC")
    fun getTransactionsOrderedById(): Flow<List<Transaction?>>

    @Query("SELECT* FROM `transaction` WHERE transaction_id=:id")
    abstract fun getTransactionById(id: Int): Flow<Transaction?>
    fun getTransactionByIdDistinctUntilChanged(id: Int) = getTransactionById(id).distinctUntilChanged()

}