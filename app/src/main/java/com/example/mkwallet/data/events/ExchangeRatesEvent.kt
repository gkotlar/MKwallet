package com.example.mkwallet.data.events

sealed interface ExchangeRatesEvent {
    data object SaveExchangeRates: ExchangeRatesEvent
    data class DeleteExchangeRates(val event: ExchangeRatesEvent): ExchangeRatesEvent
}