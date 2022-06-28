package br.com.jose.currencyconverter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CurrencyconverterApplication

fun main(args: Array<String>) {
	runApplication<CurrencyconverterApplication>(*args)
}
