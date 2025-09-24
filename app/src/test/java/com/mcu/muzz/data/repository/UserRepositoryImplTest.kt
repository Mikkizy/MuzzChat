package com.mcu.muzz.data.repository

import com.mcu.muzz.data.local.dao.UserDao
import com.mcu.muzz.data.local.entity.UserEntity
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class UserRepositoryImplTest {

    private lateinit var userDao: UserDao
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        userDao = mockk()
        repository = UserRepositoryImpl(userDao)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `getAllUsers should return domain models from entity models`() = runTest {
        // Given
        val userEntities = listOf(
            UserEntity(id = "user1", name = "Sarah", avatarUrl = null),
            UserEntity(id = "user2", name = "John", avatarUrl = "http://example.com/avatar.png")
        )
        every { userDao.getAllUsers() } returns flowOf(userEntities)

        // When
        val result = repository.getAllUsers().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("user1", result[0].id)
        assertEquals("Sarah", result[0].name)
        assertNull(result[0].avatarUrl)
        assertEquals("user2", result[1].id)
        assertEquals("John", result[1].name)
        assertEquals("http://example.com/avatar.png", result[1].avatarUrl)
        verify { userDao.getAllUsers() }
    }

    @Test
    fun `getUserById should return domain model when user exists`() = runTest {
        // Given
        val userId = "user1"
        val userEntity = UserEntity(id = userId, name = "Sarah", avatarUrl = null)
        coEvery { userDao.getUserById(userId) } returns userEntity

        // When
        val result = repository.getUserById(userId)

        // Then
        assertNotNull(result)
        assertEquals(userId, result?.id)
        assertEquals("Sarah", result?.name)
        assertNull(result?.avatarUrl)
        coVerify { userDao.getUserById(userId) }
    }

    @Test
    fun `getUserById should return null when user does not exist`() = runTest {
        // Given
        val userId = "nonexistent"
        coEvery { userDao.getUserById(userId) } returns null

        // When
        val result = repository.getUserById(userId)

        // Then
        assertNull(result)
        coVerify { userDao.getUserById(userId) }
    }

    @Test
    fun `initializeUsers should insert predefined users`() = runTest {
        // Given
        coEvery { userDao.insertUsers(any()) } just Runs

        // When
        repository.initializeUsers()

        // Then
        coVerify { userDao.insertUsers(match { users ->
            users.size == 2 &&
                    users[0].id == "user1" && users[0].name == "Sarah" &&
                    users[1].id == "user2" && users[1].name == "Miracle"
        }) }
    }
}