package com.mcu.muzz.data.repository

import com.mcu.muzz.data.local.dao.MessageDao
import com.mcu.muzz.data.local.entity.MessageEntity
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class MessageRepositoryImplTest {

    private lateinit var messageDao: MessageDao
    private lateinit var repository: MessageRepositoryImpl

    @Before
    fun setup() {
        messageDao = mockk()
        repository = MessageRepositoryImpl(messageDao)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `getAllMessages should return domain models from entity models`() = runTest {
        // Given
        val timestamp = LocalDateTime.now()
        val messageEntities = listOf(
            MessageEntity(
                id = "1",
                content = "Hello",
                senderId = "user1",
                timestamp = timestamp,
                isRead = true
            ),
            MessageEntity(
                id = "2",
                content = "Hi there",
                senderId = "user2",
                timestamp = timestamp.plusMinutes(1),
                isRead = false
            )
        )
        every { messageDao.getAllMessages() } returns flowOf(messageEntities)

        // When
        val result = repository.getAllMessages().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Hello", result[0].content)
        assertEquals("user1", result[0].senderId)
        assertEquals(timestamp, result[0].timestamp)
        assertTrue(result[0].isRead)
        assertEquals("2", result[1].id)
        assertEquals("Hi there", result[1].content)
        assertEquals("user2", result[1].senderId)
        assertEquals(timestamp.plusMinutes(1), result[1].timestamp)
        assertFalse(result[1].isRead)
        verify { messageDao.getAllMessages() }
    }

    @Test
    fun `sendMessage should create and insert message entity`() = runTest {
        // Given
        val content = "Test message"
        val senderId = "user1"
        coEvery { messageDao.insertMessage(any()) } just Runs

        // When
        repository.sendMessage(content, senderId)

        // Then
        coVerify { messageDao.insertMessage(match { entity ->
            entity.content == content &&
                    entity.senderId == senderId &&
                    !entity.isRead &&
                    entity.id.isNotEmpty()
        }) }
    }

    @Test
    fun `markMessagesAsRead should call dao with correct user id`() = runTest {
        // Given
        val currentUserId = "user1"
        coEvery { messageDao.markMessagesAsRead(any()) } just Runs

        // When
        repository.markMessagesAsRead(currentUserId)

        // Then
        coVerify { messageDao.markMessagesAsRead(currentUserId) }
    }
}