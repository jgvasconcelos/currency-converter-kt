package br.com.jose.currencyconverter.service.impl

import br.com.jose.currencyconverter.exception.ResourceAlreadyExistsException
import br.com.jose.currencyconverter.mapper.CurrencyMapper
import br.com.jose.currencyconverter.model.Currency
import br.com.jose.currencyconverter.service.CurrencyService
import org.springframework.stereotype.Service

@Service
class CurrencyServiceImpl(
    private val currencyMapper: CurrencyMapper
) : CurrencyService {
    override fun getCurrencyByShortName(shortName: String): Currency? {
        return currencyMapper.getCurrencyByShortName(shortName)
    }

    override fun getAllCurrencies(): Collection<Currency> {
        return currencyMapper.getAllCurrencies()
    }

    override fun insertCurrency(currency: Currency) {
        currencyMapper.getCurrencyByShortName(currency.shortName)?.let {
            throw ResourceAlreadyExistsException("The currency " + currency.shortName + " is already registered.")
        }
        currencyMapper.insertCurrency(currency)
    }
}