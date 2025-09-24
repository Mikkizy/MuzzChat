package com.mcu.muzz.domain.usecases

import com.mcu.muzz.domain.repository.MessageRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class MarkMessagesAsReadUseCaseTest {

    private lateinit var messageRepository: MessageRepository
    private lateinit var useCase: MarkMessagesAsReadUseCase

    @Before
    fun setup() {
        messageRepository = mockk()
        useCase = MarkMessagesAsReadUseCase(messageRepository)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should call repository with correct user id`() = runTest {
        // Given
        val currentUserId = "user1"
        coEvery { messageRepository.markMessagesAsRead(any()) } just Runs

        // When
        useCase.invoke(currentUserId)

        // Then
        coVerify { messageRepository.markMessagesAsRead(currentUserId) }
    }
}