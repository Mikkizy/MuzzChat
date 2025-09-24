package com.mcu.muzz.presentation.chat

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mcu.muzz.domain.models.User
import com.mcu.muzz.presentation.ui.theme.MuzzChatTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun chatScreen_completeUserInteraction_flow() {
        // Given
        var messageText = ""
        var switchUserCalled = false
        var sendMessageCalled = false

        val initialState = ChatUIState(
            messages = emptyList(),
            currentUser = User("user1", "Sarah"),
            otherUser = User("user2", "John"),
            isLoading = false,
            error = null
        )

        composeTestRule.setContent {
            MuzzChatTheme {
                ChatScreen(
                    onBackClick = {},
                    messageText = messageText,
                    uiState = initialState,
                    switchUser = { switchUserCalled = true },
                    sendMessage = { sendMessageCalled = true },
                    clearError = {},
                    onMessageTextChanged = { messageText = it }
                )
            }
        }

        // When & Then - Complete user interaction flow

        // 1. Verify initial state
        composeTestRule
            .onNodeWithText("John")
            .assertIsDisplayed()

        // 2. Open dropdown menu
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()

        composeTestRule
            .onNodeWithText("Switch User")
            .assertIsDisplayed()

        // 3. Switch user
        composeTestRule
            .onNodeWithText("Switch User")
            .performClick()

        assert(switchUserCalled)

        // 4. Type a message
        composeTestRule
            .onNodeWithTag("message_input")
            .performTextInput("Hello World")

        // 5. Send message
        composeTestRule
            .onNodeWithContentDescription("Send message")
            .performClick()

        assert(sendMessageCalled)
    }
}