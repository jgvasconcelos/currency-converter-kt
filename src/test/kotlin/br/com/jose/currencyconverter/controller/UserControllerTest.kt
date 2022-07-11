package br.com.jose.currencyconverter.controller

import br.com.jose.currencyconverter.exception.ResourceNotFoundException
import br.com.jose.currencyconverter.model.User
import br.com.jose.currencyconverter.service.UserService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDate

class UserControllerTest {

    private val mockedUserService: UserService = mockk()

    private val userController: UserController = UserController(mockedUserService)

    @Test
    fun`if the controller calls the service and returns a user by id`() {
        val admin = User(1, "Pedrin", "c6bank", LocalDate.now(), true)

        val expectedResponse: ResponseEntity<User> = ResponseEntity(admin, HttpStatus.OK)

        every { mockedUserService.getUserById(admin.id) } returns admin

        val result = userController.getUserById(admin.id)

        assertEquals(expectedResponse, result)

    }

    @Test
    fun`Throws exception if it doesn't find a user by id`() {
        val admin = User(1, "Pedrin", "c6bank", LocalDate.now(), true)

        every { mockedUserService.getUserById(admin.id) } returns null

        val resultAssert = assertThrows<ResourceNotFoundException> {
            userController.getUserById(admin.id)
        }

        assertEquals("User not found.", resultAssert.message)
    }

    @Test
    fun`if the controller calls the service and returns a user by user name`() {
        val admin = User(1, "Pedrin", "c6bank", LocalDate.now(), true)

        val returnedResponse: ResponseEntity<User> = ResponseEntity(admin, HttpStatus.OK)

        every { mockedUserService.getUserByUsername(admin.username) } returns admin

        val result = userController.getUserByUsername(admin.username)

        assertEquals(returnedResponse, result)
    }

    @Test
    fun`Throws exception if it doesn't find a user by user name`() {
        val admin = User(1, "Pedrin", "c6bank", LocalDate.now(), true)

        every { mockedUserService.getUserByUsername(admin.username) } returns null

        val resultAssert = assertThrows<ResourceNotFoundException> {
            userController.getUserByUsername(admin.username)
        }

        assertEquals("User not found.", resultAssert.message)
    }

    @Test
    fun`If the controller calls the service and inserts a user into the database`() {
        val admin = User(1, "Pedrin", "c6bank", LocalDate.now(), true)

        val returnedResponse: ResponseEntity<User> = ResponseEntity(admin, HttpStatus.CREATED)

        every { mockedUserService.insertUser(admin) } just runs

        val result = userController.postUser(admin)

        //dúvida, aqui não deveria ser feito o verify (exactly = 1)?
        assertEquals(returnedResponse, result)

    }


}