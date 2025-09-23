package com.mcu.muzz.presentation.components

/*@Composable
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
}*/

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
