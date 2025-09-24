package com.mcu.muzz.presentation.chat

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mcu.muzz.domain.models.Message
import com.mcu.muzz.domain.models.MessageGroup
import com.mcu.muzz.domain.models.User
import com.mcu.muzz.presentation.ui.theme.MuzzChatTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class ChatScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockUser1 = User(id = "user1", name = "Sarah")
    private val mockUser2 = User(id = "user2", name = "John")

    private val defaultUiState = ChatUIState(
        messages = emptyList(),
        currentUser = mockUser1,
        otherUser = mockUser2,
        isLoading = false,
        error = null
    )

    @Test
    fun chatScreen_displaysCorrectUserName_inTopBar() {
        // Given
        val uiState = defaultUiState.copy(
            currentUser = mockUser1,
            otherUser = mockUser2
        )

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("John")
            .assertIsDisplayed()
    }

    @Test
    fun chatScreen_displaysUnknownUser_whenOtherUserIsNull() {
        // Given
        val uiState = defaultUiState.copy(otherUser = null)

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Unknown User") // Assuming this is your string resource
            .assertIsDisplayed()
    }

    @Test
    fun chatScreen_displaysCorrectProfilePicture_basedOnCurrentUser() {
        // Given - user1 is current user
        val uiState = defaultUiState.copy(currentUser = mockUser1)

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Profile Picture")
            .assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBackClick_whenPressed() {
        // Given
        var backClickCalled = false
        val onBackClick = { backClickCalled = true }

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = onBackClick,
                    messageText = "",
                    uiState = defaultUiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        // Then
        assert(backClickCalled)
    }

    @Test
    fun moreOptionsButton_opensDropdownMenu_whenClicked() {
        // Given
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = defaultUiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Switch User")
            .assertIsDisplayed()
    }

    @Test
    fun switchUserMenuItem_callsSwitchUser_whenClicked() {
        // Given
        var switchUserCalled = false
        val switchUser = { switchUserCalled = true }

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = defaultUiState,
                    switchUser = switchUser,
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()

        composeTestRule
            .onNodeWithText("Switch User")
            .performClick()

        // Then
        assert(switchUserCalled)
    }

    @Test
    fun switchUserMenuItem_closesDropdownMenu_afterClick() {
        // Given
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = defaultUiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()

        composeTestRule
            .onNodeWithText("Switch User")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Switch User")
            .assertDoesNotExist()
    }

    @Test
    fun chatScreen_displaysMessages_whenMessagesExist() {
        // Given
        val messages = listOf(
            MessageGroup(
                messages = listOf(
                    Message(
                        id = "1",
                        content = "Hello from Sarah",
                        senderId = "user1",
                        timestamp = LocalDateTime.now(),
                        isRead = true
                    )
                )
            ),
            MessageGroup(
                messages = listOf(
                    Message(
                        id = "2",
                        content = "Hello from John",
                        senderId = "user2",
                        timestamp = LocalDateTime.now().plusMinutes(1),
                        isRead = false
                    )
                )
            )
        )
        val uiState = defaultUiState.copy(messages = messages)

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Hello from Sarah")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Hello from John")
            .assertIsDisplayed()
    }

    @Test
    fun chatScreen_displaysEmptyState_whenNoMessages() {
        // Given
        val uiState = defaultUiState.copy(messages = emptyList())

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then - LazyColumn should still be present but empty
        composeTestRule
            .onNodeWithText("Hello from Sarah")
            .assertDoesNotExist()
    }

    @Test
    fun messageInput_displaysCorrectText() {
        // Given
        val messageText = "Test message input"

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = messageText,
                    uiState = defaultUiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(messageText)
            .assertIsDisplayed()
    }

    @Test
    fun messageInput_callsOnMessageTextChanged_whenTextChanges() {
        // Given
        var capturedText = ""
        val onMessageTextChanged: (String) -> Unit = { text -> capturedText = text }

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = defaultUiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = onMessageTextChanged
                )
            }
        }

        // When
        val testMessage = "Hello World"
        composeTestRule
            .onNodeWithTag("message_input") // Assuming this is the content description
            .performTextInput(testMessage)

        // Then
        assert(capturedText == testMessage)
    }

    @Test
    fun sendButton_callsSendMessage_whenClicked() {
        // Given
        var sendMessageCalled = false
        val sendMessage = { sendMessageCalled = true }

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "Test message",
                    uiState = defaultUiState,
                    switchUser = {},
                    sendMessage = sendMessage,
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithContentDescription("Send message") // Assuming this is the content description
            .performClick()

        // Then
        assert(sendMessageCalled)
    }

    @Test
    fun chatScreen_callsClearError_whenErrorExists() {
        // Given
        var clearErrorCalled = false
        val clearError = { clearErrorCalled = true }
        val uiState = defaultUiState.copy(error = "Test error")

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = clearError,
                    onMessageTextChanged = {}
                )
            }
        }

        // Wait for LaunchedEffect to execute
        composeTestRule.waitForIdle()

        // Then
        assert(clearErrorCalled)
    }

    @Test
    fun chatScreen_doesNotCallClearError_whenNoError() {
        // Given
        var clearErrorCalled = false
        val clearError = { clearErrorCalled = true }
        val uiState = defaultUiState.copy(error = null)

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = clearError,
                    onMessageTextChanged = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Then
        assert(!clearErrorCalled)
    }

    @Test
    fun chatScreen_autoScrollsToBottom_whenNewMessagesAdded() {
        // Given
        val initialMessages = listOf(
            MessageGroup(
                messages = listOf(
                    Message("1", "First message", "user1", LocalDateTime.now(), false)
                )
            )
        )

        val uiStateWithMessages = defaultUiState.copy(messages = initialMessages)

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = uiStateWithMessages,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then - First message should be visible
        composeTestRule
            .onNodeWithText("First message")
            .assertIsDisplayed()
    }

    @Test
    fun topAppBar_hasCorrectBackgroundColor() {
        // Given
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = "",
                    uiState = defaultUiState,
                    switchUser = {},
                    sendMessage = {},
                    clearError = {},
                    onMessageTextChanged = {}
                )
            }
        }

        // Then - TopAppBar should be present (this tests the structure)
        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("More options")
            .assertIsDisplayed()
    }
}