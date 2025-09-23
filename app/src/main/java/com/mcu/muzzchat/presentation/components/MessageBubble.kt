package com.mcu.muzzchat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.presentation.ui.theme.MessageBubbleReceived
import com.mcu.muzzchat.presentation.ui.theme.MessageBubbleSent
import com.mcu.muzzchat.presentation.ui.theme.MuzzChatTheme
import java.time.LocalDateTime

@Composable
fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val maxWidth = (configuration.screenWidthDp * 0.75).dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        contentAlignment = if (message.isFromCurrentUser) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isFromCurrentUser) 16.dp else 1.dp,
                        bottomEnd = if (message.isFromCurrentUser) 1.dp else 16.dp
                    )
                )
                .background(
                    if (message.isFromCurrentUser) {
                        MessageBubbleSent
                    } else {
                        MessageBubbleReceived
                    }
                )
                .padding(12.dp)
        ) {
            Text(
                text =" message.text",
                color = if (message.isFromCurrentUser) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun MessageBubblePreview() {
    MuzzChatTheme {
        Column {
            MessageBubble(
                message = Message(
                    id = "1",
                    text = "Hello there! This is a sample message from the current user.",
                    timestamp = LocalDateTime.now(),
                    isFromCurrentUser = true,
                    senderName = "You"
                )
            )
            MessageBubble(
                message = Message(
                    id = "2",
                    text = "Hi! This is a response from Sarah.",
                    timestamp = LocalDateTime.now(),
                    isFromCurrentUser = false,
                    senderName = "Sarah"
                )
            )
        }
    }
}*/
