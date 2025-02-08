package com.example.mkwallet.data.events

import com.example.mkwallet.data.models.Transaction

sealed interface AccountEvent {
    data object SaveAccount: AccountEvent
    data object ShowDialog: AccountEvent
    data object HideDialog: AccountEvent
    data class DeleteAccount(val account: AccountEvent): AccountEvent

}