package com.mcu.muzz.presentation.utils

import com.mcu.muzz.domain.models.Message
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun formatMessageTime(timestamp: Instant): String {
    val now = Clock.System.now()
    val messageTime = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
    val currentTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    val messageDate = messageTime.date
    val currentDate = currentTime.date

    return when {
        messageDate == currentDate -> "Today"
        messageDate == currentDate.minus(1, DateTimeUnit.DAY) -> "Yesterday"
        else -> {
            val dayOfWeek = messageDate.dayOfWeek
            when (dayOfWeek) {
                DayOfWeek.MONDAY -> "Monday"
                DayOfWeek.TUESDAY -> "Tuesday"
                DayOfWeek.WEDNESDAY -> "Wednesday"
                DayOfWeek.THURSDAY -> "Thursday"
                DayOfWeek.FRIDAY -> "Friday"
                DayOfWeek.SATURDAY -> "Saturday"
                DayOfWeek.SUNDAY -> "Sunday"
            }
        }
    } + " ${messageTime.hour.toString().padStart(2, '0')}:${messageTime.minute.toString().padStart(2, '0')}"
}

sealed class MessageItem {
    data class Section(val text: String) : MessageItem()
    data class MessageGroup(
        val messages: List<Message>,
        val isFromCurrentUser: Boolean
    ) : MessageItem()
}

/*fun groupMessagesWithSections(messages: List<Message>): List<MessageItem> {
    if (messages.isEmpty()) return emptyList()

    val result = mutableListOf<MessageItem>()
    var currentGroup = mutableListOf<Message>()
    var lastSender: Boolean? = null
    var lastTimestamp: LocalDateTime? = null

    messages.forEach { message ->
        // Check if we need a section header
        val needsSection = lastTimestamp == null ||
                ChronoUnit.HOURS.between(lastTimestamp, message.timestamp) >= 1

        if (needsSection) {
            // Add previous group if exists
            if (currentGroup.isNotEmpty() && lastSender != null) {
                result.add(MessageItem.MessageGroup(currentGroup.toList(), lastSender))
                currentGroup.clear()
            }

            // Add section header
            result.add(MessageItem.Section(formatSectionHeader(message.timestamp)))
        }

        // Check if we need to start a new group
        val needsNewGroup = lastSender != message.isFromCurrentUser ||
                (lastTimestamp != null && ChronoUnit.SECONDS.between(lastTimestamp, message.timestamp) >= 20)

        if (needsNewGroup && currentGroup.isNotEmpty() && lastSender != null) {
            result.add(MessageItem.MessageGroup(currentGroup.toList(), lastSender))
            currentGroup.clear()
        }

        currentGroup.add(message)
        lastSender = message.isFromCurrentUser
        lastTimestamp = message.timestamp
    }

    // Add final group
    if (currentGroup.isNotEmpty() && lastSender != null) {
        result.add(MessageItem.MessageGroup(currentGroup.toList(), lastSender))
    }

    return result
}*/

fun formatSectionHeader(timestamp: LocalDateTime): String {
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val day = timestamp.format(dayFormatter)
    val time = timestamp.format(timeFormatter)

    return "$day $time"
}