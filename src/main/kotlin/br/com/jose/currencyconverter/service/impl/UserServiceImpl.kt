package br.com.jose.currencyconverter.service.impl

import br.com.jose.currencyconverter.exception.ResourceAlreadyExistsException
import br.com.jose.currencyconverter.mapper.UserMapper
import br.com.jose.currencyconverter.model.User
import br.com.jose.currencyconverter.service.UserService
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.LocalDate
import java.util.*

@Service
class UserServiceImpl(
    private val userMapper: UserMapper
) : UserService {
    override fun getUserById(userId: Long): User? {
        return userMapper.getUserById(userId)
    }

    override fun getUserByUsername(username: String): User? {
        return userMapper.getUserByUsername(username)
    }

    override fun insertUser(user: User) {
        getUserByUsername(user.username)?.let {
            throw ResourceAlreadyExistsException("User " + user.username + " is already registered.")
        }

        val passwordByteArray = MessageDigest.getInstance("SHA-256").digest(user.password.toByteArray())
        val passwordSHA256 = Base64.getEncoder().encodeToString(passwordByteArray)

        user.password = passwordSHA256
        user.registerDate = LocalDate.now()
        userMapper.insertUser(user)
    }
}