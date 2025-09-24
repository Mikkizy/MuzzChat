package com.mcu.muzz.domain.usecases

import com.mcu.muzz.domain.repository.MessageRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SendMessageUseCaseTest {

    private lateinit var messageRepository: MessageRepository
    private lateinit var sendMessageUseCase: SendMessageUseCase

    @Before
    fun setup() {
        messageRepository = mockk()
        sendMessageUseCase = SendMessageUseCase(messageRepository)
    }

    @Test
    fun `when content is not blank, should call repository sendMessage`() = runTest {
        // Given
        val content = "Hello World"
        val senderId = "user1"
        coEvery { messageRepository.sendMessage(any(), any()) } just Runs

        // When
        sendMessageUseCase(content, senderId)

        // Then
        coVerify { messageRepository.sendMessage(content, senderId) }
    }

    @Test
    fun `when content is blank, should not call repository sendMessage`() = runTest {
        // Given
        val content = "   "
        val senderId = "user1"

        // When
        sendMessageUseCase(content, senderId)

        // Then
        coVerify(exactly = 0) { messageRepository.sendMessage(any(), any()) }
    }

    @Test
    fun `should trim content before sending`() = runTest {
        // Given
        val content = "  Hello World  "
        val senderId = "user1"
        coEvery { messageRepository.sendMessage(any(), any()) } just Runs

        // When
        sendMessageUseCase(content, senderId)

        // Then
        coVerify { messageRepository.sendMessage("Hello World", senderId) }
    }
}