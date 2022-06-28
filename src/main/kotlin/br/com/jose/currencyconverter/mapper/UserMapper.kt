package br.com.jose.currencyconverter.mapper

import br.com.jose.currencyconverter.model.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface UserMapper {
    fun getUserById(@Param("userId") userId: Long): User?
    fun insertUser(@Param("user") user: User)
    fun getUserByUsername(@Param("username") username: String): User?
}