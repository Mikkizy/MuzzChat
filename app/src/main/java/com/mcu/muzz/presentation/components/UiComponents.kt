package com.mcu.muzz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.mcu.muzz.R
import com.mcu.muzz.domain.models.Message
import com.mcu.muzz.domain.models.MessageGroup
import com.mcu.muzz.presentation.ui.theme.DarkGray
import com.mcu.muzz.presentation.ui.theme.LightGray
import com.mcu.muzz.presentation.ui.theme.MessageBubbleReceived
import com.mcu.muzz.presentation.ui.theme.MessageBubbleSent
import com.mcu.muzz.presentation.ui.theme.PrimaryPink
import com.mcu.muzz.presentation.ui.theme.SecondaryPink
import com.mcu.muzz.presentation.ui.theme.Yellow

@Composable
fun MessageGroupItem(
    messageGroup: MessageGroup,
    currentUserId: String
) {
    Column {
        // Timestamp
        if (messageGroup.shouldShowTimestamp && messageGroup.timestamp != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                val annotatedString = buildAnnotatedString {
                    val lastSpaceIndex = messageGroup.timestamp.lastIndexOf(' ')

                    if (lastSpaceIndex != -1 && lastSpaceIndex < messageGroup.timestamp.length - 1) {
                        val dayPart = messageGroup.timestamp.substring(0, lastSpaceIndex)
                        val timePart = messageGroup.timestamp.substring(lastSpaceIndex + 1)

                        // Style for the Day part
                        withStyle(
                            style = SpanStyle(
                                color = LightGray,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(dayPart)
                        }

                        append(" ")

                        // Style for the Time part
                        withStyle(
                            style = SpanStyle(
                                color = LightGray
                            )
                        ) {
                            append(timePart)
                        }
                    } else {
                        // Fallback if the format is unexpected, just append the original text
                        // with default section header styling.
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append(messageGroup.timestamp)
                        }
                    }
                }

                Text(
                    text = annotatedString,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Messages
        messageGroup.messages.forEachIndexed { index, message ->
            MessageBubble(
                message = message,
                isFromCurrentUser = message.senderId == currentUserId
            )
            if (index < messageGroup.messages.size - 1) {
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: Message,
    isFromCurrentUser: Boolean
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
                    MessageBubbleSent
                } else {
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
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                SubcomposeLayout { constraints ->
                    val textPlaceable = subcompose("text") {
                        Text(
                            text = message.content,
                            color = if (isFromCurrentUser) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }[0].measure(constraints)

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
                            }
                        }
                    }.map {
                        // Constrain the width of the icon Row to be exactly the width of the Text
                        it.measure(Constraints.fixedWidth(textPlaceable.width))
                    }

                    // Calculate the total height needed
                    val totalHeight = textPlaceable.height + (iconRowPlaceable.firstOrNull()?.height ?: 0) +
                            if (isFromCurrentUser && iconRowPlaceable.isNotEmpty()) 4.dp.roundToPx() else 0

                    layout(textPlaceable.width, totalHeight) {
                        textPlaceable.placeRelative(0, 0)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(
    messageText: String,
    onMessageTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit
) {
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
            value = messageText,
            onValueChange = onMessageTextChanged,
            modifier = Modifier
                .weight(1f)
                .imePadding(),
            placeholder = { Text(stringResource(R.string.type_message)) },
            shape = RoundedCornerShape(24.dp),
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (messageText.isNotBlank())PrimaryPink else LightGray,
                unfocusedBorderColor = LightGray,
                disabledBorderColor = DarkGray,
                focusedLabelColor = PrimaryPink,
                unfocusedLabelColor = SecondaryPink,
                disabledLabelColor = SecondaryPink
            ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        FloatingActionButton(
            onClick = onSendMessage,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .imePadding(),
            containerColor = if (messageText.isNotBlank()) PrimaryPink else SecondaryPink
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.send_message),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}