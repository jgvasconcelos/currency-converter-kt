package br.com.jose.currencyconverter.service.impl

import br.com.jose.currencyconverter.exception.ResourceAlreadyExistsException
import br.com.jose.currencyconverter.mapper.UserMapper
import br.com.jose.currencyconverter.model.User
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class UserServiceImplTest {

    private val mockedUserMapper: UserMapper = mockk()

    private val userServiceImpl: UserServiceImpl = UserServiceImpl(mockedUserMapper)

    @Test
    fun `Returns a user successfully when searching by id`() {
        val admin = User(1, "Zevas", "JJ", LocalDate.now(), true)

        every { mockedUserMapper.getUserById(admin.id) } returns admin

        val result = userServiceImpl.getUserById(admin.id)

        assertEquals(admin, result)
    }

    @Test
    fun `Returns null when searching for id and not finding a user`() {
        every { mockedUserMapper.getUserById(2) } returns null

        val result = userServiceImpl.getUserById(2)

        assertEquals(null, result)
    }

    @Test
    fun `Returns a user successfully when searching by username`() {
        val admin = User(1, "JoseGil", "Zevas", LocalDate.now(), true)

        every { mockedUserMapper.getUserByUsername(admin.username) } returns admin

        val result = userServiceImpl.getUserByUsername(admin.username)

        assertEquals(admin, result)
    }

    @Test
    fun `Returns null when searching for username and not finding a user`() {
        every { mockedUserMapper.getUserByUsername("Gil") } returns null

        val result = userServiceImpl.getUserByUsername("Gil")

        assertEquals(null, result)
    }

    @Test
    fun `If the user is saved correctly`() {
        val admin = User(1, "JoseGil", "Zevas", LocalDate.now(), true)

        every { mockedUserMapper.insertUser(admin) } just runs
        every { mockedUserMapper.getUserByUsername(admin.username) } returns null

        userServiceImpl.insertUser(admin)

        verify (exactly = 1) { mockedUserMapper.insertUser(admin) }
    }

    @Test
    fun `Throws an exception when the user already exists`() {
        val admin = User(1, "JoseGil", "Zevas", LocalDate.now(), true)

        every { mockedUserMapper.insertUser(admin) } just runs
        every { mockedUserMapper.getUserByUsername(admin.username) } returns admin

        val resultAssert = assertThrows<ResourceAlreadyExistsException> {
            userServiceImpl.insertUser(admin) }

        assertEquals("User JoseGil is already registered.", resultAssert.message)
    }

}