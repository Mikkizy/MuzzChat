package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.repository.MessageRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class GetMessagesUseCaseTest {

    private lateinit var messageRepository: MessageRepository
    private lateinit var getMessagesUseCase: GetMessagesUseCase

    @Before
    fun setUp() {
        messageRepository = mockk()
        getMessagesUseCase = GetMessagesUseCase(messageRepository)
    }

    @Test
    fun `when invoke called, should return messages flow from repository`() = runTest {
        // Given
        val messages = listOf(
            Message(
                id = 1,
                text = "Hello",
                timestamp = LocalDateTime.now(),
                isFromCurrentUser = true,
                isRead = false
            ),
            Message(
                id = 2,
                text = "Hi there",
                timestamp = LocalDateTime.now(),
                isFromCurrentUser = false,
                isRead = true
            )
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = getMessagesUseCase().first()

        // Then
        verify { messageRepository.getAllMessages() }
        assert(result == messages)
        assert(result.size == 2)
        assert(result[0].text == "Hello")
        assert(result[1].text == "Hi there")
    }

    @Test
    fun `when invoke called with empty repository, should return empty list`() = runTest {
        // Given
        every { messageRepository.getAllMessages() } returns flowOf(emptyList())

        // When
        val result = getMessagesUseCase().first()

        // Then
        verify { messageRepository.getAllMessages() }
        assert(result.isEmpty())
    }
}