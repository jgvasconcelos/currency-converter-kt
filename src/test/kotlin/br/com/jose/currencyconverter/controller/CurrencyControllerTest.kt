package br.com.jose.currencyconverter.controller

import br.com.jose.currencyconverter.exception.ResourceNotFoundException
import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.service.impl.CurrencyServiceImpl
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CurrencyControllerTest {

    private val mockedCurrencyServiceImpl: CurrencyServiceImpl = mockk()

    private val currencyController: CurrencyController = CurrencyController(mockedCurrencyServiceImpl)

    @Test
    fun`If the controller calls the service and returns all currencies`() {
        val brCoin = Currency(1, "Real Brasileiro,", "BRL")
        val usaCoin = Currency(2, "Dolar Americano", "USD")

        val currencies = mutableListOf(brCoin, usaCoin)

        val expectedResponse: ResponseEntity<Collection<Currency>> =
            ResponseEntity<Collection<Currency>>(currencies, HttpStatus.OK)

        every { mockedCurrencyServiceImpl.getAllCurrencies() } returns currencies

        val result = currencyController.getAllCurrencies()

        assertEquals(expectedResponse, result)

    }

    @Test
    fun`If the controller calls the service and returns a currency from the short name`() {
        val brCoin = Currency(1, "Real Brasileiro,", "BRL")

        val expectedResponse: ResponseEntity<Currency> = ResponseEntity<Currency>(brCoin, HttpStatus.OK)

        every { mockedCurrencyServiceImpl.getCurrencyByShortName(brCoin.shortName) } returns brCoin

        val result = currencyController.getCurrencyByShortName(brCoin.shortName)

        assertEquals(expectedResponse, result)

    }

    @Test
    fun`Throws exception if it doesn't find a currency from the short name`() {
        val brCoin = Currency(1, "Real Brasileiro,", "BRL")

        every { mockedCurrencyServiceImpl.getCurrencyByShortName(brCoin.shortName) } returns null

        val resultAssert = assertThrows<ResourceNotFoundException> {
            currencyController.getCurrencyByShortName(brCoin.shortName)
        }

        assertEquals("Currency not found.", resultAssert.message)
    }

    @Test
    fun`If the controller calls the service and inserts a currency into the database`() {
        val brCoin = Currency(1, "Real Brasileiro,", "BRL")

        val returnedResponse: ResponseEntity<Currency> = ResponseEntity<Currency>(brCoin, HttpStatus.CREATED)

        every { mockedCurrencyServiceImpl.insertCurrency(brCoin) } just runs

        val result = currencyController.postCurrency(brCoin)

        //dúvida, aqui não deveria ser feito o verify (exactly = 1)?
        assertEquals(returnedResponse, result)

    }

}