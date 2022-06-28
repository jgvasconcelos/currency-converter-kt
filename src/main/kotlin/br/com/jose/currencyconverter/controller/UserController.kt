package br.com.jose.currencyconverter.controller

import br.com.jose.currencyconverter.exception.ResourceNotFoundException
import br.com.jose.currencyconverter.model.User
import br.com.jose.currencyconverter.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") userId: Long): ResponseEntity<User> {
        userService.getUserById(userId)?.let {
            return ResponseEntity.ok().body(it)
        }

        throw ResourceNotFoundException("User not found.")
    }

    @GetMapping
    fun getUserByUsername(@RequestParam("username") username: String): ResponseEntity<User> {
        userService.getUserByUsername(username)?.let {
            return ResponseEntity.ok().body(it)
        }

        throw ResourceNotFoundException("User not found.")
    }

    @PostMapping
    fun postUser(@RequestBody user: User): ResponseEntity<User> {
        userService.insertUser(user)

        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }
}