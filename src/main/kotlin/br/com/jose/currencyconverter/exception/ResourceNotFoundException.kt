package br.com.jose.currencyconverter.exception

class ResourceNotFoundException(
    private val msg: String
) : Exception(msg)