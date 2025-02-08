package com.example.mkwallet.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithTransactions(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "currency",
        entityColumn = "transaction_currency"
    )
    val transactions: List<Transaction>
)