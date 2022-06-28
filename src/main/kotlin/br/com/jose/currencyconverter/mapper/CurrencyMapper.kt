package br.com.jose.currencyconverter.mapper

import br.com.jose.currencyconverter.model.Currency
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface CurrencyMapper {
    fun getCurrencyByShortName(@Param("shortName") shortName: String): Currency?
    fun getCurrencyById(@Param("id") id: Long): Currency?
    fun getAllCurrencies(): Collection<Currency>
    fun insertCurrency(@Param("currency") currency: Currency)
}