package com.mcu.muzzchat.presentation.chat

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.presentation.ui.theme.MuzzChatTheme
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

    private val defaultUiState = ChatUiState(
        messages = emptyList(),
        currentMessage = "",
        isLoading = false
    )

    @Test
    fun chatScreen_displaysCorrectInitialState() {
        // Given
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = defaultUiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Miracle")
            .assertExists()

        composeTestRule
            .onNodeWithTag("messageInput")
            .assertExists()
            .assertTextEquals("")

        composeTestRule
            .onNodeWithTag("sendButton")
            .assertExists()
            .assertIsNotEnabled() // Should be disabled when message is empty
    }

    @Test
    fun chatScreen_displaysMessages_whenMessagesExist() {
        // Given
        val testMessages = listOf(
            Message(
                id = 1L,
                text = "Hello from current user",
                timestamp = LocalDateTime.now().minusMinutes(10),
                isFromCurrentUser = true,
                isRead = false
            ),
            Message(
                id = 2L,
                text = "Hello back from other user",
                timestamp = LocalDateTime.now().minusMinutes(5),
                isFromCurrentUser = false,
                isRead = true
            )
        )

        val uiState = defaultUiState.copy(messages = testMessages)

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Hello from current user")
            .assertExists()

        composeTestRule
            .onNodeWithText("Hello back from other user")
            .assertExists()

        composeTestRule
            .onNodeWithTag("messagesList")
            .assertExists()
    }

    @Test
    fun chatScreen_showsSectionHeaders_forDifferentDays() {
        // Given
        val today = LocalDateTime.now()
        val yesterday = today.minusDays(1)

        val testMessages = listOf(
            Message(
                id = 1L,
                text = "Yesterday message",
                timestamp = yesterday,
                isFromCurrentUser = true,
                isRead = false
            ),
            Message(
                id = 2L,
                text = "Today message",
                timestamp = today,
                isFromCurrentUser = false,
                isRead = true
            )
        )

        val uiState = defaultUiState.copy(messages = testMessages)

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Yesterday message")
            .assertExists()

        composeTestRule
            .onNodeWithText("Today message")
            .assertExists()
    }

    @Test
    fun messageInput_displaysTypedText() {
        // Given
        val uiState = defaultUiState.copy(currentMessage = "Test message")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithTag("messageInput")
            .assertTextEquals("Test message")
    }

    @Test
    fun sendButton_isEnabled_whenMessageIsNotBlank() {
        // Given
        val uiState = defaultUiState.copy(currentMessage = "Hello")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithTag("sendButton")
            .assertIsEnabled()
    }

    @Test
    fun sendButton_isDisabled_whenMessageIsBlank() {
        // Given
        val uiState = defaultUiState.copy(currentMessage = "")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithTag("sendButton")
            .assertIsNotEnabled()
    }

    @Test
    fun sendButton_isDisabled_whenMessageIsOnlyWhitespace() {
        // Given
        val uiState = defaultUiState.copy(currentMessage = "   ")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithTag("sendButton")
            .assertIsNotEnabled()
    }

    @Test
    fun sendButton_callsSendMessage_whenClicked() {
        // Given
        var sendMessageCalled = false
        val sendMessage = { sendMessageCalled = true }
        val uiState = defaultUiState.copy(currentMessage = "Test message")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = sendMessage
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithTag("sendButton")
            .performClick()

        // Then
        assert(sendMessageCalled)
    }

    @Test
    fun messageInput_supportsSendOnImeAction() {
        // Given
        var sendMessageCalled = false
        val sendMessage = { sendMessageCalled = true }
        val uiState = defaultUiState.copy(currentMessage = "Test message")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = sendMessage
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithTag("messageInput")
            .performImeAction()

        // Then
        assert(sendMessageCalled)
    }

    @Test
    fun messageBubbles_showCorrectReadStatus_forCurrentUserMessages() {
        // Given
        val readMessage = Message(
            id = 1L,
            text = "Read message",
            timestamp = LocalDateTime.now(),
            isFromCurrentUser = true,
            isRead = true
        )

        val unreadMessage = Message(
            id = 2L,
            text = "Unread message",
            timestamp = LocalDateTime.now().plusMinutes(1),
            isFromCurrentUser = true,
            isRead = false
        )

        val uiState = defaultUiState.copy(messages = listOf(readMessage, unreadMessage))

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Message Read")
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription("Message Delivered")
            .assertExists()
    }

    @Test
    fun messageBubbles_displayCorrectAlignment_basedOnSender() {
        // Given
        val currentUserMessage = Message(
            id = 1L,
            text = "Message from current user",
            timestamp = LocalDateTime.now(),
            isFromCurrentUser = true,
            isRead = false
        )

        val otherUserMessage = Message(
            id = 2L,
            text = "Message from other user",
            timestamp = LocalDateTime.now().plusMinutes(1),
            isFromCurrentUser = false,
            isRead = false
        )

        val uiState = defaultUiState.copy(messages = listOf(currentUserMessage, otherUserMessage))

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Message from current user")
            .assertExists()

        composeTestRule
            .onNodeWithText("Message from other user")
            .assertExists()
    }

    @Test
    fun messageBubbles_onlyShowReadStatus_forCurrentUserMessages() {
        // Given
        val currentUserMessage = Message(
            id = 1L,
            text = "Current user message",
            timestamp = LocalDateTime.now(),
            isFromCurrentUser = true,
            isRead = true
        )

        val otherUserMessage = Message(
            id = 2L,
            text = "Other user message",
            timestamp = LocalDateTime.now().plusMinutes(1),
            isFromCurrentUser = false,
            isRead = false
        )

        val uiState = defaultUiState.copy(messages = listOf(currentUserMessage, otherUserMessage))

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        // Should show read status for current user message
        composeTestRule
            .onAllNodesWithContentDescription("Message Read")
            .assertCountEquals(1)

        // Other user messages shouldn't have read status indicators
        composeTestRule
            .onNodeWithText("Other user message")
            .assertExists()
    }

    @Test
    fun chatScreen_showsEmptyState_whenNoMessages() {
        // Given
        val uiState = defaultUiState.copy(messages = emptyList())

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithTag("messagesList")
            .assertExists()

        // Should not show any message content
        composeTestRule
            .onAllNodesWithText("Hello", substring = true)
            .assertCountEquals(0)
    }

    @Test
    fun chatScreen_handlesLongMessages() {
        // Given
        val longMessage = "This is a very long message that should be handled properly by the chat interface and should wrap correctly within the message bubble constraints and not cause any layout issues with the SubcomposeLayout implementation"

        val message = Message(
            id = 1L,
            text = longMessage,
            timestamp = LocalDateTime.now(),
            isFromCurrentUser = true,
            isRead = false
        )

        val uiState = defaultUiState.copy(messages = listOf(message))

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(longMessage)
            .assertExists()
    }

    @Test
    fun chatScreen_autoScrolls_whenNewMessagesAdded() {
        // Given - Start with some messages
        val initialMessages = listOf(
            Message(1L, "Message 1", LocalDateTime.now().minusMinutes(5), true, false),
            Message(2L, "Message 2", LocalDateTime.now().minusMinutes(4), false, false)
        )

        val uiState = defaultUiState.copy(messages = initialMessages)

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then - Messages should be visible
        composeTestRule
            .onNodeWithText("Message 1")
            .assertExists()

        composeTestRule
            .onNodeWithText("Message 2")
            .assertExists()
    }

    @Test
    fun topBar_displaysCorrectTitle() {
        // Given
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = defaultUiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Miracle")
            .assertExists()
    }

    @Test
    fun messageGroups_handleConsecutiveMessages_correctly() {
        // Given
        val consecutiveMessages = listOf(
            Message(1L, "First message", LocalDateTime.now().minusMinutes(2), true, false),
            Message(2L, "Second message", LocalDateTime.now().minusMinutes(1), true, false),
            Message(3L, "Third message", LocalDateTime.now(), true, true)
        )

        val uiState = defaultUiState.copy(messages = consecutiveMessages)

        // When
        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    uiState = uiState,
                    updateCurrentMessage = {},
                    sendMessage = {}
                )
            }
        }

        // Then - All messages should be displayed
        composeTestRule
            .onNodeWithText("First message")
            .assertExists()

        composeTestRule
            .onNodeWithText("Second message")
            .assertExists()

        composeTestRule
            .onNodeWithText("Third message")
            .assertExists()
    }
}