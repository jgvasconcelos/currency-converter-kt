package br.com.jose.currencyconverter.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ConversionRatesDTO(
    val base: String,
    val date: LocalDate,
    val rates: Map<String, BigDecimal>,
    val success: Boolean
)
