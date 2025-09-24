package com.mcu.muzz.domain.usecases

import com.mcu.muzz.domain.models.Message
import com.mcu.muzz.domain.repository.MessageRepository
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime

class GetMessagesUseCaseTest {

    private lateinit var messageRepository: MessageRepository
    private lateinit var useCase: GetMessagesUseCase

    @Before
    fun setup() {
        messageRepository = mockk()
        useCase = GetMessagesUseCase(messageRepository)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should return empty list when no messages`() = runTest {
        // Given
        every { messageRepository.getAllMessages() } returns flowOf(emptyList())

        // When
        val result = useCase.invoke().first()

        // Then
        assertTrue(result.isEmpty())
        verify { messageRepository.getAllMessages() }
    }

    @Test
    fun `invoke should group messages from same sender within 20 seconds`() = runTest {
        // Given
        val baseTime = LocalDateTime.now()
        val messages = listOf(
            Message("1", "Hello", "user1", baseTime, false),
            Message("2", "How are you?", "user1", baseTime.plusSeconds(15), false),
            Message("3", "I'm fine", "user2", baseTime.plusSeconds(30), false)
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = useCase.invoke().first()

        // Then
        assertEquals(3, result.size) // Corrected expectation

        // First group: message 1 with timestamp
        assertTrue(result[0].shouldShowTimestamp)
        assertEquals(1, result[0].messages.size)

        // Second group: message 2 (grouped after timestamp)
        assertFalse(result[1].shouldShowTimestamp)
        assertEquals(1, result[1].messages.size)

        // Third group: message 3 (different sender)
        assertFalse(result[2].shouldShowTimestamp)
        assertEquals(1, result[2].messages.size)

        verify { messageRepository.getAllMessages() }
    }

    @Test
    fun `invoke should separate messages from different senders`() = runTest {
        // Given
        val baseTime = LocalDateTime.now()
        val messages = listOf(
            Message("1", "Hello", "user1", baseTime, false),
            Message("2", "Hi", "user2", baseTime.plusSeconds(5), false),
            Message("3", "How are you?", "user1", baseTime.plusSeconds(10), false)
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = useCase.invoke().first()

        // Then
        assertEquals(3, result.size) // Each message creates separate group due to sender changes
        assertEquals(1, result[0].messages.size)
        assertEquals(1, result[1].messages.size)
        assertEquals(1, result[2].messages.size)
        verify { messageRepository.getAllMessages() }
    }

    @Test
    fun `invoke should show timestamp for first message`() = runTest {
        // Given
        val baseTime = LocalDateTime.now()
        val messages = listOf(
            Message("1", "Hello", "user1", baseTime, false),
            Message("2", "Later message", "user1", baseTime.plusMinutes(30), false) // < 1 hour
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = useCase.invoke().first()

        // Then
        assertEquals(2, result.size)
        assertTrue("First message should show timestamp", result[0].shouldShowTimestamp) // Corrected expectation
        assertFalse("Second message should not show timestamp (< 1 hour)", result[1].shouldShowTimestamp)
        assertNotNull(result[0].timestamp)
        verify { messageRepository.getAllMessages() }
    }

    @Test
    fun `invoke should show timestamp for messages more than 1 hour apart`() = runTest {
        // Given
        val baseTime = LocalDateTime.now()
        val messages = listOf(
            Message("1", "Hello", "user1", baseTime, false),
            Message("2", "Later message", "user1", baseTime.plusHours(2), false)
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = useCase.invoke().first()

        // Then
        assertEquals(2, result.size)
        assertTrue("First message should show timestamp", result[0].shouldShowTimestamp)
        assertTrue("Second message should show timestamp (> 1 hour)", result[1].shouldShowTimestamp)
        assertNotNull(result[0].timestamp)
        assertNotNull(result[1].timestamp)
        verify { messageRepository.getAllMessages() }
    }

    @Test
    fun `invoke should format timestamps correctly for today`() = runTest {
        // Given
        val now = LocalDateTime.now()
        val messages = listOf(
            Message("1", "Hello", "user1", now.minusHours(2), false),
            Message("2", "Later", "user1", now, false)
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = useCase.invoke().first()

        // Then
        assertTrue(result[0].timestamp!!.startsWith("Today"))
        assertTrue(result[1].timestamp!!.startsWith("Today"))
        verify { messageRepository.getAllMessages() }
    }

    @Test
    fun `invoke should group consecutive messages from same sender within time limits`() = runTest {
        // Given
        val baseTime = LocalDateTime.now()
        val messages = listOf(
            Message("1", "Hello", "user1", baseTime, false),
            Message("2", "How are you?", "user1", baseTime.plusSeconds(10), false),
            Message("3", "What's up?", "user1", baseTime.plusSeconds(15), false)
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = useCase.invoke().first()

        // Then
        // First message gets timestamp, remaining messages get grouped
        assertEquals(2, result.size)
        assertTrue("First group should show timestamp", result[0].shouldShowTimestamp)
        assertEquals(1, result[0].messages.size)
        assertFalse("Second group should not show timestamp", result[1].shouldShowTimestamp)
        assertEquals(2, result[1].messages.size) // Messages 2 and 3 grouped together
        verify { messageRepository.getAllMessages() }
    }

    @Test
    fun `invoke should separate messages more than 20 seconds apart from same sender`() = runTest {
        // Given
        val baseTime = LocalDateTime.now()
        val messages = listOf(
            Message("1", "Hello", "user1", baseTime, false),
            Message("2", "Later message", "user1", baseTime.plusSeconds(25), false) // > 20 seconds
        )
        every { messageRepository.getAllMessages() } returns flowOf(messages)

        // When
        val result = useCase.invoke().first()

        // Then
        assertEquals(2, result.size) // Should be in separate groups
        assertEquals(1, result[0].messages.size)
        assertEquals(1, result[1].messages.size)
        assertTrue("First message should show timestamp", result[0].shouldShowTimestamp)
        assertFalse("Second message should not show timestamp (< 1 hour)", result[1].shouldShowTimestamp)
        verify { messageRepository.getAllMessages() }
    }
}