package com.mcu.muzzchat.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mcu.muzzchat.R
import com.mcu.muzzchat.presentation.components.ChatTopBar
import com.mcu.muzzchat.presentation.components.MessageBubbleGroup
import com.mcu.muzzchat.presentation.components.SectionHeader
import com.mcu.muzzchat.presentation.ui.theme.DarkGray
import com.mcu.muzzchat.presentation.ui.theme.LightGray
import com.mcu.muzzchat.presentation.ui.theme.PrimaryPink
import com.mcu.muzzchat.presentation.ui.theme.SecondaryPink
import com.mcu.muzzchat.presentation.utils.MessageItem
import com.mcu.muzzchat.presentation.utils.groupMessagesWithSections
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    uiState: ChatUiState,
    updateCurrentMessage: (String) -> Unit,
    sendMessage: () -> Unit
) {
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Track keyboard visibility
    val isKeyboardVisible = WindowInsets.isImeVisible

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size, isKeyboardVisible) {
        if (uiState.messages.isNotEmpty()) {
            delay(100)
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        ChatTopBar(
            userName = stringResource(id = R.string.default_contact_name),
            onBackClick = onBackClick,
            onMoreClick = { /* Does nothing for this case. */ }
        )

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("messagesList"),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val groupedMessages = groupMessagesWithSections(uiState.messages)

            items(groupedMessages) { item ->
                when (item) {
                    is MessageItem.Section -> {
                        SectionHeader(text = item.text)
                    }
                    is MessageItem.MessageGroup -> {
                        MessageBubbleGroup(
                            messages = item.messages,
                            isFromCurrentUser = item.isFromCurrentUser
                        )
                    }
                }
            }
        }

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .imePadding(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.currentMessage,
                onValueChange = updateCurrentMessage,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("messageInput")
                    .imePadding(),
                enabled = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        sendMessage()
                        keyboardController?.hide()
                    }
                ),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (uiState.currentMessage.isNotBlank())PrimaryPink else LightGray,
                    unfocusedBorderColor = LightGray,
                    disabledBorderColor = DarkGray,
                    focusedLabelColor = PrimaryPink,
                    unfocusedLabelColor = SecondaryPink,
                    disabledLabelColor = SecondaryPink
                ),
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .imePadding()
                    .background(if (uiState.currentMessage.isNotBlank()) PrimaryPink else SecondaryPink),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        sendMessage()
                        keyboardController?.hide()
                    },
                    modifier = Modifier.testTag("sendButton"),
                    enabled = uiState.currentMessage.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(id = R.string.send_message),
                        tint = Color.White
                    )
                }
            }
        }
    }
}