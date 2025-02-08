package com.example.mkwallet.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.mkwallet.data.models.Account
import com.example.mkwallet.data.models.AccountWithTransactions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface AccountDao {

    @Upsert
    suspend fun upsertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT * FROM account ORDER BY currency ASC")
    fun getAccountsOrderedById(): Flow<List<Account?>>

    @Query("SELECT* FROM account WHERE currency=:currency")
    fun getAccountById(currency: String): Flow<Account?>
    fun getAccountByIdDistinctUntilChanged(currency: String) = getAccountById(currency).distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM account")
    suspend fun getAll(): List<AccountWithTransactions>

    @Transaction
    @Query("SELECT * FROM account WHERE currency = :currency")
    suspend fun getByTransactionId(currency: String): AccountWithTransactions
}