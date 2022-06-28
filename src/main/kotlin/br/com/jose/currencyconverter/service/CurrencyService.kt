package br.com.jose.currencyconverter.service

import br.com.jose.currencyconverter.model.Currency

interface CurrencyService {
    fun getCurrencyByShortName(shortName: String): Currency?
    fun getAllCurrencies(): Collection<Currency>
    fun insertCurrency(currency: Currency)
}