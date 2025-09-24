package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.repository.MessageRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class SendMessageUseCaseTest {

    private lateinit var messageRepository: MessageRepository
    private lateinit var sendMessageUseCase: SendMessageUseCase

    @Before
    fun setUp() {
        messageRepository = mockk(relaxed = true)
        sendMessageUseCase = SendMessageUseCase(messageRepository)
    }

    @Test
    fun `when invoke with message text and sender flag, should insert message to repository`() = runTest {
        // Given
        val messageText = "Hello, World!"
        val isFromCurrentUser = true
        val messageSlot = slot<Message>()

        // When
        sendMessageUseCase(messageText, isFromCurrentUser)

        // Then
        coVerify { messageRepository.insertMessage(capture(messageSlot)) }
        assert(messageSlot.captured.text == messageText)
        assert(messageSlot.captured.isFromCurrentUser == isFromCurrentUser)
        assert(messageSlot.captured.timestamp.isBefore(LocalDateTime.now().plusSeconds(1)))
        assert(messageSlot.captured.timestamp.isAfter(LocalDateTime.now().minusSeconds(1)))
    }
}