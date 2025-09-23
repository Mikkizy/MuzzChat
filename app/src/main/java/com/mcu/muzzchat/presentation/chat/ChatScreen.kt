package com.mcu.muzzchat.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mcu.muzzchat.R
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.presentation.components.ChatTopBar
import com.mcu.muzzchat.presentation.ui.theme.DarkGray
import com.mcu.muzzchat.presentation.ui.theme.LightGray
import com.mcu.muzzchat.presentation.ui.theme.MessageBubbleReceived
import com.mcu.muzzchat.presentation.ui.theme.MessageBubbleSent
import com.mcu.muzzchat.presentation.ui.theme.PrimaryPink
import com.mcu.muzzchat.presentation.ui.theme.SecondaryPink
import com.mcu.muzzchat.presentation.ui.theme.Yellow
import com.mcu.muzzchat.presentation.utils.MessageItem
import com.mcu.muzzchat.presentation.utils.groupMessagesWithSections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
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
            onMoreClick = { /* Handle more options */ }
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
                onValueChange = viewModel::updateCurrentMessage,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("messageInput")
                    .imePadding(),
                enabled = true,
                placeholder = {
                    Text(text = stringResource(R.string.message_hint))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        viewModel.sendMessage()
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

            //Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .imePadding()
                    .background(if (uiState.currentMessage.isNotBlank()) PrimaryPink else SecondaryPink),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        viewModel.sendMessage()
                        keyboardController?.hide()
                    },
                    modifier = Modifier.testTag("sendButton"),
                    enabled = uiState.currentMessage.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = stringResource(id = R.string.send_message),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) { // text is like "Thursday 11:59"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val annotatedString = buildAnnotatedString {
            // Assuming the format is always "DayOfWeek HH:mm"
            // Find the last space, which separates the day from the time
            val lastSpaceIndex = text.lastIndexOf(' ')

            if (lastSpaceIndex != -1 && lastSpaceIndex < text.length - 1) {
                val dayPart = text.substring(0, lastSpaceIndex)
                val timePart = text.substring(lastSpaceIndex + 1)

                // Style for the Day part
                withStyle(
                    style = SpanStyle(
                        color = LightGray, // Custom Dark Gray
                        fontWeight = FontWeight.Bold // Bold
                    )
                ) {
                    append(dayPart)
                }

                append(" ") // Add the space back

                // Style for the Time part
                withStyle(
                    style = SpanStyle(
                        color = LightGray // Custom Light Gray
                        // fontWeight = FontWeight.Normal (default)
                    )
                ) {
                    append(timePart)
                }
            } else {
                // Fallback if the format is unexpected, just append the original text
                // with default section header styling
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append(text)
                }
            }
        }

        Text(
            text = annotatedString,
            modifier = Modifier.align(Alignment.Center),
            // You can keep some base styling here, or let AnnotatedString fully control
            style = MaterialTheme.typography.bodySmall.copy(
                // These will be overridden by SpanStyle for color/fontWeight where specified
                // color = MaterialTheme.colorScheme.onSurfaceVariant,
                // fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MessageBubbleGroup(
    messages: List<Message>,
    isFromCurrentUser: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        messages.forEachIndexed { index, message ->
            MessageBubble(
                message = message,
                isFromCurrentUser = isFromCurrentUser,
                isLast = index == messages.size - 1
            )

            if (index < messages.size - 1) {
                Spacer(modifier = Modifier.height(2.dp))
            } else {
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    isFromCurrentUser: Boolean,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isFromCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(
                    start = if (isFromCurrentUser) 48.dp else 0.dp,
                    end = if (!isFromCurrentUser) 48.dp else 0.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isFromCurrentUser) {
                    //MaterialTheme.colorScheme.primary
                    MessageBubbleSent
                } else {
                    //MaterialTheme.colorScheme.surfaceVariant
                    MessageBubbleReceived
                }
            ),
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (!isFromCurrentUser ) 1.dp else 12.dp,
                bottomEnd = if (isFromCurrentUser ) 1.dp else 12.dp
            )
        ) {
            /*Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (isFromCurrentUser) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.bodyMedium
            )*/
            // Use SubcomposeLayout to measure Text then size the Icon Row
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                SubcomposeLayout { constraints ->
                    // First Pass: Measure the Text content
                    val textPlaceable = subcompose("text") {
                        Text(
                            text = message.text,
                            color = if (isFromCurrentUser) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }[0].measure(constraints) // Measure with incoming constraints (from Card)

                    // Second Pass: Measure the Icon Row using the Text's width
                    val iconRowPlaceable = subcompose("iconRow") {
                        if (isFromCurrentUser) { // Only show icon row for current user
                            Row(
                                modifier = Modifier
                                    .padding(top = 4.dp), // Padding between text and icon
                                horizontalArrangement = Arrangement.End, // Align icon to the end of this Row
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_done_all),
                                    contentDescription = if (message.isRead) {
                                        stringResource(R.string.message_read)
                                    } else {
                                        stringResource(R.string.message_delivered)
                                    },
                                    tint = if (message.isRead) Yellow else LightGray,
                                    modifier = Modifier.size(12.dp)
                                )
                                /*if (message.isRead) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = stringResource(R.string.message_read),
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier
                                            .size(12.dp)
                                            .offset(x = (-4).dp)
                                    )
                                }*/
                            }
                        }
                    }.map {
                        // Constrain the width of the icon Row to be exactly the width of the Text
                        it.measure(Constraints.fixedWidth(textPlaceable.width))
                    }

                    // Calculate the total height needed
                    val totalHeight = textPlaceable.height + (iconRowPlaceable.firstOrNull()?.height ?: 0) +
                            if (isFromCurrentUser && iconRowPlaceable.isNotEmpty()) 4.dp.roundToPx() else 0 // Add padding if iconRow exists

                    layout(textPlaceable.width, totalHeight) {
                        // Place the Text
                        textPlaceable.placeRelative(0, 0)

                        // Place the Icon Row below the Text
                        iconRowPlaceable.firstOrNull()?.placeRelative(
                            0,
                            textPlaceable.height + if (isFromCurrentUser) 4.dp.roundToPx() else 0
                        )
                    }
                }
            }
        }

        if (isFromCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

