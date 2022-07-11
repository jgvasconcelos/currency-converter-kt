package br.com.jose.currencyconverter.service.impl

import br.com.jose.currencyconverter.exception.ResourceAlreadyExistsException
import br.com.jose.currencyconverter.mapper.CurrencyMapper
import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.service.impl.CurrencyServiceImpl
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CurrencyServiceImplTest {

    private val mockedCurrencyMapper: CurrencyMapper = mockk()

    private val currencyServiceImpl: CurrencyServiceImpl = CurrencyServiceImpl(mockedCurrencyMapper)

    @Test
    fun `if return a currency with abbreviated name`() {
        val brCoin = Currency(1,"Real Brasileiro", "BRL")

        every { mockedCurrencyMapper.getCurrencyByShortName(brCoin.shortName) } returns brCoin

        val result = currencyServiceImpl.getCurrencyByShortName(brCoin.shortName)

        assertEquals(brCoin, result)

    }

    @Test
    fun `Returns null, when it does not find a currency by its short name`() {
        val shortName = "USD"

        every { mockedCurrencyMapper.getCurrencyByShortName(shortName) } returns null

        val result = currencyServiceImpl.getCurrencyByShortName(shortName)

        assertEquals(null, result)
    }


    @Test
    fun `if all coins are returned`() {
        val brCoin = Currency(1, "Real Brasileiro", "BRL")
        val dolarCoin = Currency(2, "Dolar Americano", "USD")

        val allCurrencies = mutableListOf(brCoin, dolarCoin) //ou allCurrencies.add()

        every { mockedCurrencyMapper.getAllCurrencies() } returns allCurrencies

        val returnedList = currencyServiceImpl.getAllCurrencies()

        assertEquals(allCurrencies, returnedList)

    }

    @Test
    fun `if the service calls the repository when inserting the currency`() {
        val brCoin = Currency(1, "Real Brasileiro", "BRL")
        //val dolarCoin = Currency(2, "Dolar Americano", "USD")

        every { mockedCurrencyMapper.insertCurrency(brCoin) } just runs
        every { mockedCurrencyMapper.getCurrencyByShortName(brCoin.shortName) } returns null

        currencyServiceImpl.insertCurrency(brCoin)

        verify (exactly = 1) { mockedCurrencyMapper.insertCurrency(brCoin) }

    }

    @Test
    fun `Throws exception when short name is already registered`() {
        val brCoin = Currency(1, "Real Brasileiro", "BRL")

        every { mockedCurrencyMapper.getCurrencyByShortName(brCoin.shortName) } returns brCoin

        val resultAssert = assertThrows<ResourceAlreadyExistsException> {
            currencyServiceImpl.insertCurrency(brCoin)
        }

        assertEquals("The currency BRL is already registered.", resultAssert.message)
    }

}