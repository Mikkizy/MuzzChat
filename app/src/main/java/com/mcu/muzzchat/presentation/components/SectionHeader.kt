package com.mcu.muzzchat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mcu.muzzchat.presentation.ui.theme.LightGray

@Composable
fun SectionHeader(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val annotatedString = buildAnnotatedString {
            val lastSpaceIndex = text.lastIndexOf(' ')

            if (lastSpaceIndex != -1 && lastSpaceIndex < text.length - 1) {
                val dayPart = text.substring(0, lastSpaceIndex)
                val timePart = text.substring(lastSpaceIndex + 1)

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
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )
    }
}