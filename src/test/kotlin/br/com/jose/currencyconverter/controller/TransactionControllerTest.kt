package br.com.jose.currencyconverter.controller

import br.com.jose.currencyconverter.exception.ResourceNotFoundException
import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.model.Transaction
import br.com.jose.currencyconverter.model.User
import br.com.jose.currencyconverter.service.TransactionService
import br.com.jose.currencyconverter.service.UserService
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class TransactionControllerTest {

    private val mockedTransactionService: TransactionService = mockk()

    private val mockedUserService: UserService = mockk()

    private val transactionController: TransactionController = TransactionController(
        mockedTransactionService, mockedUserService
    )

    @Test
    fun`If the controller calls the service, it creates, saves and returns the transaction`() {
        val originCurrency = Currency(1000, "Real Brasileiro", "BRL")
        val targetCurrency = Currency(2001, "Dolar Americano", "USD")

        val admin = User(100, "Pedrin", "c6bank", LocalDate.now(), true)

        val originValue = BigDecimal(100.0)
        val conversionRate = BigDecimal(5.0)

        val localDateTime = LocalDateTime.now()

        val transaction = Transaction(
            1,
            originCurrency,
            originValue,
            targetCurrency,
            conversionRate,
            localDateTime,
            admin
        )

        val expectedResponse: ResponseEntity<Transaction> = ResponseEntity(transaction, HttpStatus.OK)

        every {
            mockedTransactionService.createAndInsertTransaction(
                originCurrency.shortName,
                targetCurrency.shortName,
                originValue,
                admin.id
            )
        } returns transaction

        val result = transactionController.getTransaction(
            originCurrency.shortName,
            targetCurrency.shortName,
            originValue,
            admin.id
        )

        verify (exactly = 1) {
            mockedTransactionService.createAndInsertTransaction(
                originCurrency.shortName,
                targetCurrency.shortName,
                originValue,
                admin.id
            )
        }

        assertEquals(expectedResponse, result)
    }

    @Test
    fun`If the controller calls the service, it looks for the user id and returns its transactions`() {
        val originCurrency = Currency(1000, "Real Brasileiro", "BRL")
        val targetDollarCurrency = Currency(2001, "Dolar Americano", "USD")
        val targetEuroCurrency = Currency(2002, "Euro", "EUR")

        val admin = User(100, "Pedrin", "c6bank", LocalDate.now(), true)

        val originValue = BigDecimal(100.0)
        val conversionRate = BigDecimal(5.0)

        val transactionDateTime = LocalDateTime.now()

        val transactionConversionToDollar = Transaction(
            1,
            originCurrency,
            originValue,
            targetDollarCurrency,
            conversionRate,
            transactionDateTime,
            admin
        )

        val transactionConversionToEuro = Transaction(
            1,
            originCurrency,
            originValue,
            targetEuroCurrency,
            conversionRate,
            transactionDateTime,
            admin
        )

        val transactionList = mutableListOf(transactionConversionToDollar, transactionConversionToEuro)

        val expectedResponse: ResponseEntity<Collection<Transaction>> = ResponseEntity (
            transactionList, HttpStatus.OK
        )

        every { mockedUserService.getUserById(admin.id) } returns admin

        every { mockedTransactionService.getTransactionsByUserId(admin.id) } returns transactionList

        val result = transactionController.getTransactionsByUserId(admin.id)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun`throws exception when it doesn't find a user by id`() {
        val admin = User(100, "Pedrin", "c6bank", LocalDate.now(), true)

        every { mockedUserService.getUserById(admin.id) } returns null

        val resultAssert = assertThrows<ResourceNotFoundException> {
            transactionController.getTransactionsByUserId(admin.id)
        }

        assertEquals("User not found.", resultAssert.message)

    }

}