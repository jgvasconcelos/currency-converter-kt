package br.com.jose.currencyconverter.service.impl

import br.com.jose.currencyconverter.dto.ConversionRatesDTO
import br.com.jose.currencyconverter.exception.TransactionBadRequestException
import br.com.jose.currencyconverter.mapper.TransactionMapper
import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.model.Transaction
import br.com.jose.currencyconverter.service.CurrencyService
import br.com.jose.currencyconverter.service.TransactionService
import br.com.jose.currencyconverter.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionServiceImpl(
    val transactionMapper: TransactionMapper,
    val userService: UserService,
    val currencyService: CurrencyService,
    val restTemplate: RestTemplate,
    @Value("\${conversion.base-url}")
    val apiBaseUrl: String,
    @Value("\${conversion.api-key}")
    val apiKey: String
) : TransactionService {

    override fun createTransaction(
        originalCurrencyShortName: String,
        targetCurrencyShortName: String,
        originalValue: BigDecimal,
        userId: Long
    ): Transaction? {
        val originalCurrency =
            currencyService.getCurrencyByShortName(originalCurrencyShortName) ?: throw TransactionBadRequestException(
                "The origin currency $originalCurrencyShortName is not registered."
            )
        val targetCurrency =
            currencyService.getCurrencyByShortName(targetCurrencyShortName) ?: throw TransactionBadRequestException(
                "The destination currency $targetCurrencyShortName is not registered."
            )
        val user = userService.getUserById(userId)
            ?: throw TransactionBadRequestException("The user with the ID $userId is not registered.")

        val conversionRate = getConversionRate(originalCurrency, targetCurrency)

        return Transaction(
            originalCurrency = originalCurrency,
            targetCurrency = targetCurrency,
            user = user,
            conversionRate = conversionRate,
            originalValue = originalValue,
            transactionDateTime = LocalDateTime.now()
        )
    }

    override fun insertTransaction(transaction: Transaction) {
        transactionMapper.insertTransaction(transaction)
    }

    override fun getConversionRate(originalCurrency: Currency, targetCurrency: Currency): BigDecimal {
        try {
            val url = "${apiBaseUrl}symbols=${targetCurrency.shortName}&base=${originalCurrency.shortName}"
            //symbols= | base= <-> Query Params

            val headers = HttpHeaders()
            headers.set("apikey", apiKey)

            val request = HttpEntity<Unit>(headers)
            //request { url, headers, method, body }

            val response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                ConversionRatesDTO::class.java
            )

            return response.body?.rates!![targetCurrency.shortName] ?: throw TransactionBadRequestException(
                "There is no available conversion rate between " + originalCurrency.shortName
                        + " and " + targetCurrency.shortName + "."
            )
        } catch (httpClientErrorException: HttpClientErrorException) {
            throw TransactionBadRequestException(httpClientErrorException.message!!)
        }
    }

    override fun getTransactionsByUserId(userId: Long): Collection<Transaction> {
        return transactionMapper.getTransactionsByUserId(userId)
    }

    override fun createAndInsertTransaction(
        originalCurrencyShortName: String,
        targetCurrencyShortName: String,
        originalValue: BigDecimal,
        userId: Long
    ): Transaction? {
        val transaction = createTransaction(
            originalCurrencyShortName,
            targetCurrencyShortName,
            originalValue,
            userId
        )
        insertTransaction(transaction!!)
        return transaction
    }

}