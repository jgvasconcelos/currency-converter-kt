package br.com.jose.currencyconverter.mapper

import br.com.jose.currencyconverter.model.Transaction
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface TransactionMapper {
    fun getTransactionsByUserId(@Param("userId") userId: Long): Collection<Transaction>
    fun insertTransaction(@Param("transaction") transaction: Transaction)
}