package br.com.jose.currencyconverter.service

import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.model.Transaction
import java.math.BigDecimal

interface TransactionService {
    fun createTransaction(
        originalCurrencyShortName: String,
        targetCurrencyShortName: String,
        originalValue: BigDecimal,
        userId: Long
    ): Transaction?
    fun insertTransaction(transaction: Transaction)
    fun getConversionRate(originalCurrency: Currency, targetCurrency: Currency): BigDecimal
    fun getTransactionsByUserId(userId: Long): Collection<Transaction>
}