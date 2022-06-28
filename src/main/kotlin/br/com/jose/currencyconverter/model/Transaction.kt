package br.com.jose.currencyconverter.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    var id: Long? = null,
    @JsonIgnore
    val originalCurrency: Currency,
    val originalValue: BigDecimal,
    @JsonIgnore
    val targetCurrency: Currency,
    val conversionRate: BigDecimal,
    val transactionDateTime: LocalDateTime,
    val user: User
) {
    @JsonProperty("originalCurrency")
    private fun getOriginalCurrencyShortName() = originalCurrency.shortName
    @JsonProperty("targetCurrency")
    private fun getTargetCurrencyShortName() = targetCurrency.shortName
    @JsonProperty("userId")
    private fun getUserId() = user.id
    @JsonProperty("targetValue")
    private fun getTargetValue() = originalValue.multiply(conversionRate)
}
