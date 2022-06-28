package br.com.jose.currencyconverter.controller

import br.com.jose.currencyconverter.exception.ResourceNotFoundException
import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.service.CurrencyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/currencies")
class CurrencyController(
    private val currencyService: CurrencyService
) {
    @GetMapping
    fun getAllCurrencies(): ResponseEntity<Collection<Currency>> {
        val currencies = currencyService.getAllCurrencies()
        return ResponseEntity.ok().body(currencies)
    }

    @GetMapping("/{shortName}")
    fun getCurrencyByShortName(@PathVariable("shortName") shortName: String): ResponseEntity<Currency> {
        val currency = currencyService.getCurrencyByShortName(shortName)?.let {
            ResponseEntity.ok().body(it)
        }
        throw ResourceNotFoundException("Currency not found.")
    }

    @PostMapping
    fun postCurrency(@RequestBody currency: Currency): ResponseEntity<Currency> {
        currencyService.insertCurrency(currency)
        return ResponseEntity.status(HttpStatus.CREATED).body(currency)
    }
}