package com.mcu.muzz.presentation.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.mcu.muzz.domain.models.Message
import com.mcu.muzz.domain.models.MessageGroup
import com.mcu.muzz.domain.models.User
import com.mcu.muzz.domain.repository.UserRepository
import com.mcu.muzz.domain.usecases.GetMessagesUseCase
import com.mcu.muzz.domain.usecases.MarkMessagesAsReadUseCase
import com.mcu.muzz.domain.usecases.SendMessageUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class ChatViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var getMessagesUseCase: GetMessagesUseCase
    private lateinit var sendMessageUseCase: SendMessageUseCase
    private lateinit var markMessagesAsReadUseCase: MarkMessagesAsReadUseCase
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: ChatViewModel

    private val testUsers = listOf(
        User("user1", "Sarah"),
        User("user2", "John")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getMessagesUseCase = mockk()
        sendMessageUseCase = mockk()
        markMessagesAsReadUseCase = mockk()
        userRepository = mockk()

        every { getMessagesUseCase() } returns flowOf(emptyList())
        every { userRepository.getAllUsers() } returns flowOf(testUsers)
        coEvery { userRepository.initializeUsers() } just Runs
        coEvery { markMessagesAsReadUseCase.invoke(any()) } just Runs

        viewModel = ChatViewModel(
            getMessagesUseCase,
            sendMessageUseCase,
            markMessagesAsReadUseCase,
            userRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `initial state should have correct users`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(testUsers[0], state.currentUser) // user1 is initial current user
            assertEquals(testUsers[1], state.otherUser)   // user2 is other user
            assertFalse(state.isLoading)
            assertNull(state.error)
        }
    }

    @Test
    fun `onMessageTextChanged should update messageText state`() = runTest {
        // Given
        val newText = "Hello World"

        // When
        viewModel.onMessageTextChanged(newText)

        // Then
        viewModel.messageText.test {
            assertEquals(newText, awaitItem())
        }
    }

    @Test
    fun `sendMessage with valid text should call use case and clear text`() = runTest {
        // Given
        val messageText = "Test message"
        coEvery { sendMessageUseCase.invoke(any(), any()) } just Runs

        // When
        viewModel.onMessageTextChanged(messageText)
        viewModel.sendMessage()

        // Then
        coVerify { sendMessageUseCase.invoke(messageText, "user1") }
        viewModel.messageText.test {
            assertEquals("", awaitItem())
        }
    }

    @Test
    fun `sendMessage with blank text should not call use case`() = runTest {
        // Given
        val messageText = "   "
        viewModel.onMessageTextChanged(messageText)

        // When
        viewModel.sendMessage()

        // Then
        coVerify(exactly = 0) { sendMessageUseCase.invoke(any(), any()) }
    }

    @Test
    fun `sendMessage with error should set error state`() = runTest {
        // Given
        val messageText = "Test message"
        val errorMessage = "Network error"
        viewModel.onMessageTextChanged(messageText)
        coEvery { sendMessageUseCase.invoke(any(), any()) } throws Exception(errorMessage)

        // When
        viewModel.sendMessage()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `switchUser should change current user and mark messages as read`() = runTest {
        // When
        viewModel.switchUser()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("user2", state.currentUser?.id)
            assertEquals("user1", state.otherUser?.id)
        }
        coVerify { markMessagesAsReadUseCase.invoke("user2") }
    }

    @Test
    fun `clearError should set error to null`() = runTest {
        // Given - set an error first
        coEvery { sendMessageUseCase.invoke(any(), any()) } throws Exception("Test error")
        viewModel.onMessageTextChanged("test")
        viewModel.sendMessage()

        // When
        viewModel.clearError()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }

    @Test
    fun `initialization should call initializeUsers and markMessagesAsRead`() {
        // Then - verify initialization calls were made
        coVerify { userRepository.initializeUsers() }
        coVerify { markMessagesAsReadUseCase.invoke("user1") }
    }

    @Test
    fun `uiState should combine all data correctly`() = runTest {
        // Given
        val messages = listOf(
            MessageGroup(
                messages = listOf(
                    Message("1", "Hello", "user1", LocalDateTime.now(), false)
                )
            )
        )
        every { getMessagesUseCase() } returns flowOf(messages)

        // Create new viewModel to get the updated messages
        val newViewModel = ChatViewModel(
            getMessagesUseCase,
            sendMessageUseCase,
            markMessagesAsReadUseCase,
            userRepository
        )

        // Then
        newViewModel.uiState.test {
            val state = awaitItem()
            assertEquals(messages, state.messages)
            assertEquals(testUsers[0], state.currentUser)
            assertEquals(testUsers[1], state.otherUser)
            assertFalse(state.isLoading)
            assertNull(state.error)
        }
    }
}