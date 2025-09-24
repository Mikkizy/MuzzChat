package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.repository.MessageRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MarkMessagesAsReadUseCaseTest {

    private lateinit var messageRepository: MessageRepository
    private lateinit var markMessagesAsReadUseCase: MarkMessagesAsReadUseCase

    @Before
    fun setUp() {
        messageRepository = mockk(relaxed = true)
        markMessagesAsReadUseCase = MarkMessagesAsReadUseCase(messageRepository)
    }

    @Test
    fun `when invoke with messageId, should mark message as read in repository`() = runTest {
        // Given
        val messageId = 123L

        // When
        markMessagesAsReadUseCase(messageId)

        // Then
        coVerify { messageRepository.markMessageAsRead(messageId) }
    }

    @Test
    fun `when markAllAsRead called, should mark all messages as read in repository`() = runTest {
        // When
        markMessagesAsReadUseCase.markAllAsRead()

        // Then
        coVerify { messageRepository.markAllMessagesAsRead() }
    }
}