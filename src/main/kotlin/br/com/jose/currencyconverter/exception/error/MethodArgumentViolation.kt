package br.com.jose.currencyconverter.exception.error

data class MethodArgumentViolation(
    val field: String,
    val message: String
)
