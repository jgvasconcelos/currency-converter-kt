package br.com.jose.currencyconverter.service

import br.com.jose.currencyconverter.model.User

interface UserService {
    fun getUserById(userId: Long): User?
    fun getUserByUsername(username: String): User?
    fun insertUser(user: User)
}