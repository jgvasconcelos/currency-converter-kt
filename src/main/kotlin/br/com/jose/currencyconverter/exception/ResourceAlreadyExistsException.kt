package br.com.jose.currencyconverter.exception

class ResourceAlreadyExistsException(
    private val msg: String
) : Exception(msg)