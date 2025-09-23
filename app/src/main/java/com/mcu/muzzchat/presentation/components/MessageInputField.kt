package com.mcu.muzzchat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcu.muzzchat.R
import com.mcu.muzzchat.presentation.ui.theme.DarkGray
import com.mcu.muzzchat.presentation.ui.theme.LightGray
import com.mcu.muzzchat.presentation.ui.theme.MuzzChatTheme
import com.mcu.muzzchat.presentation.ui.theme.PrimaryPink
import com.mcu.muzzchat.presentation.ui.theme.SecondaryPink

@Composable
fun MessageInputField(
    modifier: Modifier = Modifier,
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isEnabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageTextChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(text = stringResource(id = R.string.message_placeholder))
            },
            shape = RoundedCornerShape(30.dp),
            enabled = isEnabled,
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPink,
                unfocusedBorderColor = LightGray,
                disabledBorderColor = DarkGray,
                focusedLabelColor = PrimaryPink,
                unfocusedLabelColor = SecondaryPink,
                disabledLabelColor = SecondaryPink,

            )
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(if (messageText.isNotBlank()) PrimaryPink else SecondaryPink),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onSendClick,
                enabled = isEnabled && messageText.isNotBlank()
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

@Preview(showBackground = true)
@Composable
fun MessageInputFieldPreview() {
    MuzzChatTheme {
        MessageInputField(
            messageText = "Sample message",
            onMessageTextChange = {},
            onSendClick = {}
        )
    }
}