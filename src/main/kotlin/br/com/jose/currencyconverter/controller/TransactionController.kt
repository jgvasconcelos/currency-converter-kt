package br.com.jose.currencyconverter.controller

import br.com.jose.currencyconverter.exception.ResourceNotFoundException
import br.com.jose.currencyconverter.model.Transaction
import br.com.jose.currencyconverter.service.TransactionService
import br.com.jose.currencyconverter.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionService: TransactionService,
    private val userService: UserService
) {
    @GetMapping
    fun getTransaction(
        @RequestParam("origin") originCurrency: String,
        @RequestParam("target") targetCurrency: String,
        @RequestParam("value") value: BigDecimal,
        @RequestParam("userId") userId: Long
    ): ResponseEntity<Transaction> {
        val transaction = transactionService.createTransaction(
            originCurrency,
            targetCurrency, value, userId
        )
        transactionService.insertTransaction(transaction!!)
        return ResponseEntity.ok().body(transaction)
    }

    @GetMapping("/users/{id}")
    fun getTransactionsByUserId(@PathVariable("id") userId: Long): ResponseEntity<Collection<Transaction>> {
        userService.getUserById(userId) ?: throw ResourceNotFoundException("User not found.")
        val transactions = transactionService.getTransactionsByUserId(userId)

        return ResponseEntity.ok().body(transactions)
    }
}