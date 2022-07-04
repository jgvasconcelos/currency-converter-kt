package br.com.jose.currencyconverter.service.impl

import br.com.jose.currencyconverter.exception.TransactionBadRequestException
import br.com.jose.currencyconverter.mapper.CurrencyMapper
import br.com.jose.currencyconverter.mapper.TransactionMapper
import br.com.jose.currencyconverter.mapper.UserMapper
import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.model.Transaction
import br.com.jose.currencyconverter.model.User
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class TransactionServiceImplTest {

    private val mockedTransactionMapper: TransactionMapper = mockk()
    private val mockedUserMapper: UserMapper = mockk()
    private val mockedCurrencyMapper: CurrencyMapper = mockk()

    private val userServiceImpl: UserServiceImpl = UserServiceImpl(mockedUserMapper)
    private val currencyServiceImpl: CurrencyServiceImpl = CurrencyServiceImpl(mockedCurrencyMapper)
    private val restTemplate: RestTemplate = RestTemplate()

    private val transactionServiceImpl: TransactionServiceImpl = TransactionServiceImpl(
        mockedTransactionMapper,
        userServiceImpl,
        currencyServiceImpl,
        restTemplate,
        "apiBaseUrl",
        "apiKey"
    )
    private val transactionServiceImplSpy: TransactionServiceImpl = spyk(transactionServiceImpl)

    @Test
    fun `If the transaction is saved correctly`() {
        val admin = User(99, "Zevas", "c6bank", LocalDate.now(), true)

        val originCurrency = Currency(2, "Real Brasileiro", "BRL")
        val destinationCurrency = Currency(1, "Dolar Americano", "USD")

        val originValue = BigDecimal(100.0)
        val conversionRate = BigDecimal(4.9)

        val exchange = Transaction (
            id = 1000,
            originCurrency,
            originValue,
            destinationCurrency,
            conversionRate,
            LocalDateTime.now(),
            admin
        )

        every { mockedTransactionMapper.insertTransaction(exchange) } just runs

        transactionServiceImpl.insertTransaction(exchange)

        verify (exactly = 1) { mockedTransactionMapper.insertTransaction(exchange) }

    }

    @Test
    fun `Returns transactions when searching for a UserId`() {
        val admin = User(99, "Zevas", "c6bank", LocalDate.now(), true)

        val originCurrency = Currency(2, "Real Brasileiro", "BRL")
        val destinationCurrency = Currency(1, "Dolar Americano", "USD")

        val originValue = BigDecimal(100.0)
        val conversionRate = BigDecimal(4.9)

        val exchange = Transaction (
            id = 1000,
            originCurrency,
            originValue,
            destinationCurrency,
            conversionRate,
            LocalDateTime.now(),
            admin
        )
        val transactions = mutableListOf(exchange)

        every { mockedTransactionMapper.getTransactionsByUserId(admin.id) } returns transactions

        val result = transactionServiceImpl.getTransactionsByUserId(admin.id)

        assertEquals(transactions, result)

    }

    @Test
    fun `If the transaction is created correctly`() {
        val admin = User(99, "Zevas", "c6bank", LocalDate.now(), true)

        val transactionDateTime = LocalDateTime.now()

        val originValue = BigDecimal(100.0)
        val conversionRate = BigDecimal(4.9)

        val destinationCurrency = Currency(1, "Dolar Americano", "USD")
        val originCurrency = Currency(2, "Real Brasileiro", "BRL")

        val expectedTransaction = Transaction (
            id = null,
            originCurrency,
            originValue,
            destinationCurrency,
            conversionRate,
            transactionDateTime,
            admin
        )

        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns transactionDateTime

        every {
            currencyServiceImpl.getCurrencyByShortName(originCurrency.shortName)
        } returns originCurrency

        every {
            currencyServiceImpl.getCurrencyByShortName(destinationCurrency.shortName)
        } returns destinationCurrency

        every { userServiceImpl.getUserById(admin.id) } returns admin

        every {
            transactionServiceImplSpy.getConversionRate(originCurrency, destinationCurrency)
        } returns conversionRate

        val result = transactionServiceImplSpy.createTransaction (
            originCurrency.shortName,
            destinationCurrency.shortName,
            originValue,
            admin.id
        )

        unmockkStatic(LocalDateTime::class)

        assertEquals(expectedTransaction, result)

    }

    @Test
    fun `Returns exception when it doesn't find the shortName of a origin currency`() {
        val admin = User(99, "Zevas", "c6bank", LocalDate.now(), true)

        val originValue = BigDecimal(100.0)

        val destinationCurrency = Currency(1, "Dolar Americano", "USD")
        val originCurrency = Currency(2, "Real Brasileiro", "BRL")

        every { currencyServiceImpl.getCurrencyByShortName(originCurrency.shortName) } returns null

        val resultAssert = assertThrows<TransactionBadRequestException> {
            transactionServiceImpl.createTransaction(
                originCurrency.shortName,
                destinationCurrency.shortName,
                originValue,
                admin.id
            )
        }

        assertEquals(
            "The origin currency ${originCurrency.shortName} is not registered.",
            resultAssert.message
        )

    }

    @Test
    fun `Returns exception when it doesn't find the shortName of a destination currency`() {
        val admin = User(99, "Zevas", "c6bank", LocalDate.now(), true)

        val originValue = BigDecimal(100.0)

        val destinationCurrency = Currency(1, "Dolar Americano", "USD")
        val originCurrency = Currency(2, "Real Brasileiro", "BRL")

        every { currencyServiceImpl.getCurrencyByShortName(originCurrency.shortName) } returns originCurrency

        every { currencyServiceImpl.getCurrencyByShortName(destinationCurrency.shortName) } returns null

        val resultAssert = assertThrows<TransactionBadRequestException> {
            transactionServiceImpl.createTransaction(
                originCurrency.shortName,
                destinationCurrency.shortName,
                originValue,
                admin.id
            )
        }

        assertEquals(
            "The destination currency ${destinationCurrency.shortName} is not registered.",
            resultAssert.message
        )

    }

    @Test
    fun `Returns exception when not finding UserId of a User`() {
        val admin = User(99, "Zevas", "c6bank", LocalDate.now(), true)

        val originValue = BigDecimal(100.0)

        val destinationCurrency = Currency(1, "Dolar Americano", "USD")
        val originCurrency = Currency(2, "Real Brasileiro", "BRL")

        every { currencyServiceImpl.getCurrencyByShortName(originCurrency.shortName) } returns originCurrency

        every {
            currencyServiceImpl.getCurrencyByShortName(destinationCurrency.shortName)
        } returns destinationCurrency

        every { userServiceImpl.getUserById(admin.id) } returns null

        val resultAssert = assertThrows<TransactionBadRequestException> {
            transactionServiceImpl.createTransaction(
                originCurrency.shortName,
                destinationCurrency.shortName,
                originValue,
                admin.id
            )
        }

        assertEquals(
            "The user with the ID ${admin.id} is not registered.",
            resultAssert.message
        )

    }

}