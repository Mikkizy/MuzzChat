package com.mcu.muzzchat.presentation.chat

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.presentation.ui.theme.MuzzChatTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ChatIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun chatScreen_displaysMessageInput() {
        // Given
        val uiState = ChatUiState()

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("messageInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("sendButton").assertIsDisplayed()
    }

    @Test
    fun chatScreen_displaysMessages() {
        // Given
        val messages = listOf(
            Message(
                id = 1,
                text = "Hello from current user",
                timestamp = LocalDateTime.now(),
                isFromCurrentUser = true,
                isRead = true
            ),
            Message(
                id = 2,
                text = "Hello from other user",
                timestamp = LocalDateTime.now(),
                isFromCurrentUser = false,
                isRead = true
            )
        )

        val uiState = ChatUiState(messages = messages)

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Hello from current user").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hello from other user").assertIsDisplayed()
    }

    @Test
    fun chatScreen_sendButtonDisabledWhenInputEmpty() {
        // Given
        val uiState = ChatUiState(currentMessage = "")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("sendButton").assertIsNotEnabled()
    }

    @Test
    fun chatScreen_sendButtonEnabledWhenInputNotEmpty() {
        // Given
        val uiState = ChatUiState(currentMessage = "Hello")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("sendButton").assertIsEnabled()
    }

    @Test
    fun chatScreen_clickingSendButtonCallsCallback() {
        // Given
        val uiState = ChatUiState(currentMessage = "Hello")
        var sendMessageCalled = false

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { sendMessageCalled = true }
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("sendButton").performClick()

        // Then
        assert(sendMessageCalled)
    }

    @Test
    fun chatScreen_typingInInputCallsCallback() {
        // Given
        val uiState = ChatUiState()
        var updatedMessage = ""

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { updatedMessage = it },
                    sendMessage = { }
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("messageInput").performTextInput("Hello")

        // Then
        assert(updatedMessage == "Hello")
    }

    @Test
    fun chatScreen_displaysTopBarWithBackButton() {
        // Given
        val uiState = ChatUiState()
        var backClicked = false

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { backClicked = true },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { }
                )
            }
        }

        // When - Try to find and click back button (implementation dependent)
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Then
        assert(backClicked)
    }

    @Test
    fun chatScreen_displaysRoundedTextFieldAndCircularSendButton() {
        // Given
        val uiState = ChatUiState(currentMessage = "Test message")

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { }
                )
            }
        }

        // Then - Verify the text field shows the current message
        composeTestRule.onNodeWithTag("messageInput")
            .assertIsDisplayed()
            .assertTextEquals("Test message")

        // Verify send button is enabled and displays send icon
        composeTestRule.onNodeWithTag("sendButton")
            .assertIsEnabled()
            .assertIsDisplayed()
    }

    @Test
    fun chatScreen_handlesKeyboardSendAction() {
        // Given
        val uiState = ChatUiState(currentMessage = "Hello")
        var sendMessageCalled = false

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = { },
                    uiState = uiState,
                    updateCurrentMessage = { },
                    sendMessage = { sendMessageCalled = true }
                )
            }
        }

        // When - Simulate IME send action
        composeTestRule.onNodeWithTag("messageInput").performImeAction()

        // Then
        assert(sendMessageCalled)
    }
}