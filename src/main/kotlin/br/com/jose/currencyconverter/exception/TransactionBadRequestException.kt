package br.com.jose.currencyconverter.exception

class TransactionBadRequestException(
    private val msg: String
) : Exception(msg)