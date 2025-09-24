package com.mcu.muzzchat.presentation.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.usecases.GenerateAutoReplyUseCase
import com.mcu.muzzchat.domain.usecases.GetMessagesUseCase
import com.mcu.muzzchat.domain.usecases.MarkMessagesAsReadUseCase
import com.mcu.muzzchat.domain.usecases.SendMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getMessagesUseCase: GetMessagesUseCase
    private lateinit var sendMessageUseCase: SendMessageUseCase
    private lateinit var generateAutoReplyUseCase: GenerateAutoReplyUseCase
    private lateinit var markMessagesAsReadUseCase: MarkMessagesAsReadUseCase
    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getMessagesUseCase = mockk()
        sendMessageUseCase = mockk(relaxed = true)
        generateAutoReplyUseCase = mockk()
        markMessagesAsReadUseCase = mockk(relaxed = true)

        every { getMessagesUseCase() } returns flowOf(emptyList())

        viewModel = ChatViewModel(
            getMessagesUseCase,
            sendMessageUseCase,
            generateAutoReplyUseCase,
            markMessagesAsReadUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have empty messages and empty current message`() {
        // Then
        val uiState = viewModel.uiState.value
        assert(uiState.messages.isEmpty())
        assert(uiState.currentMessage.isEmpty())
        assert(!uiState.isLoading)
    }

    @Test
    fun `when viewModel initialized, should load messages and mark all as read`() = runTest {
        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { getMessagesUseCase() }
        coVerify { markMessagesAsReadUseCase.markAllAsRead() }
    }

    @Test
    fun `when updateCurrentMessage called, should update current message in state`() = runTest {
        // Given
        val message = "Hello, World!"

        // When
        viewModel.updateCurrentMessage(message)

        // Then
        assert(viewModel.uiState.value.currentMessage == message)
    }

    @Test
    fun `when sendMessage called with valid message, should send user message and clear input`() = runTest {
        // Given
        val message = "Hello!"
        coEvery { generateAutoReplyUseCase(any()) } returns null
        viewModel.updateCurrentMessage(message)

        // When
        viewModel.sendMessage()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { sendMessageUseCase(message, true) }
        assert(viewModel.uiState.value.currentMessage.isEmpty())
    }

    @Test
    fun `when sendMessage called with blank message, should not send message`() = runTest {
        // Given
        viewModel.updateCurrentMessage("   ")

        // When
        viewModel.sendMessage()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { sendMessageUseCase(any(), any()) }
    }

    @Test
    fun `when sendOtherUserMessage called, should send message as other user and mark all as read`() = runTest {
        // Given
        val message = "How are you?"

        // When
        viewModel.sendOtherUserMessage(message)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { sendMessageUseCase(message, false) }
        coVerify { markMessagesAsReadUseCase.markAllAsRead() }
    }

    @Test
    fun `when sendOtherUserMessage called with blank message, should not send message`() = runTest {
        // Given
        val blankMessage = "   "

        // When
        viewModel.sendOtherUserMessage(blankMessage)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { sendMessageUseCase(any(), any()) }
    }

    @Test
    fun `when markMessageAsRead called, should call use case with correct messageId`() = runTest {
        // Given
        val messageId = 123L

        // When
        viewModel.markMessageAsRead(messageId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { markMessagesAsReadUseCase(messageId) }
    }

    @Test
    fun `when messages flow updates, should update ui state`() = runTest {
        // Given
        val messages = listOf(
            Message(
                id = 1,
                text = "Hello",
                timestamp = LocalDateTime.now(),
                isFromCurrentUser = true,
                isRead = true
            )
        )
        every { getMessagesUseCase() } returns flowOf(messages)

        // When
        viewModel = ChatViewModel(
            getMessagesUseCase,
            sendMessageUseCase,
            generateAutoReplyUseCase,
            markMessagesAsReadUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.uiState.value.messages == messages)
    }
}