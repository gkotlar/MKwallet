package com.example.mkwallet.data.events

import com.example.mkwallet.data.models.Transaction

sealed interface TransactionEvent {
    data object SaveTransaction: TransactionEvent
    data object ShowDialog: TransactionEvent
    data object HideDialog: TransactionEvent
    data class DeleteTransaction(val transaction: Transaction): TransactionEvent
}