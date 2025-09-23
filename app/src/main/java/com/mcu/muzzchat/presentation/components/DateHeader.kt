package com.mcu.muzzchat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcu.muzzchat.presentation.ui.theme.MuzzChatTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DateHeader(
    dateTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = formatDateHeader(dateTime),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.2f))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}

private fun formatDateHeader(dateTime: LocalDateTime): String {
    val today = LocalDateTime.now()
    val yesterday = today.minusDays(1)

    return when {
        dateTime.toLocalDate() == today.toLocalDate() -> "Today"
        dateTime.toLocalDate() == yesterday.toLocalDate() -> "Yesterday"
        else -> dateTime.format(DateTimeFormatter.ofPattern("EEEE dd/MM"))
    }
}

@Preview(showBackground = true)
@Composable
fun DateHeaderPreview() {
    MuzzChatTheme {
        DateHeader(dateTime = LocalDateTime.now())
    }
}