package com.mcu.muzzchat.data.repository

import com.mcu.muzzchat.data.local.dao.MessageDao
import com.mcu.muzzchat.data.local.entity.MessageEntity
import com.mcu.muzzchat.data.mapper.toEntity
import com.mcu.muzzchat.domain.models.Message
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class MessageRepositoryImplTest {

    private lateinit var messageDao: MessageDao
    private lateinit var repository: MessageRepositoryImpl

    @Before
    fun setUp() {
        messageDao = mockk(relaxed = true)
        repository = MessageRepositoryImpl(messageDao)
    }

    @Test
    fun `when getAllMessages called, should return mapped domain messages with read status`() = runTest {
        // Given
        val messageEntities = listOf(
            MessageEntity(
                id = 1,
                text = "Hello",
                timestamp = "2023-01-01T10:00:00",
                isFromCurrentUser = true,
                isRead = true
            ),
            MessageEntity(
                id = 2,
                text = "Hi there",
                timestamp = "2023-01-01T10:01:00",
                isFromCurrentUser = false,
                isRead = false
            )
        )
        every { messageDao.getAllMessages() } returns flowOf(messageEntities)

        // When
        val result = repository.getAllMessages().first()

        // Then
        assert(result.size == 2)
        assert(result[0].text == "Hello")
        assert(result[0].isFromCurrentUser)
        assert(result[0].isRead == true)
        assert(result[1].text == "Hi there")
        assert(result[1].isFromCurrentUser == false)
        assert(result[1].isRead == false)
    }

    @Test
    fun `when markMessageAsRead called, should call dao markMessageAsRead`() = runTest {
        // Given
        val messageId = 123L

        // When
        repository.markMessageAsRead(messageId)

        // Then
        coVerify { messageDao.markMessageAsRead(messageId) }
    }

    @Test
    fun `when markAllMessagesAsRead called, should call dao markAllMessagesAsRead`() = runTest {
        // When
        repository.markAllMessagesAsRead()

        // Then
        coVerify { messageDao.markAllMessagesAsRead() }
    }

    @Test
    fun `when insertMessage called, should insert entity to dao with correct read status`() = runTest {
        // Given
        val message = Message(
            id = 0,
            text = "Hello",
            timestamp = LocalDateTime.now(),
            isFromCurrentUser = true,
            isRead = false
        )

        // When
        repository.insertMessage(message)

        // Then
        coVerify { messageDao.insertMessage(message.toEntity()) }
    }
}