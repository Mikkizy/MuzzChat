package com.mcu.muzzchat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.mcu.muzzchat.R
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.presentation.ui.theme.LightGray
import com.mcu.muzzchat.presentation.ui.theme.MessageBubbleReceived
import com.mcu.muzzchat.presentation.ui.theme.MessageBubbleSent
import com.mcu.muzzchat.presentation.ui.theme.Yellow

@Composable
fun MessageBubbleGroup(
    messages: List<Message>,
    isFromCurrentUser: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        messages.forEachIndexed { index, message ->
            MessageBubble(
                message = message,
                isFromCurrentUser = isFromCurrentUser
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
                    }[0].measure(constraints)

                    // Second Pass: Measure the Icon Row using the Text's width
                    val iconRowPlaceable = subcompose("iconRow") {
                        if (isFromCurrentUser) {
                            Row(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.End,
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
                            if (isFromCurrentUser && iconRowPlaceable.isNotEmpty()) 4.dp.roundToPx() else 0 // Add padding if iconRow exists

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