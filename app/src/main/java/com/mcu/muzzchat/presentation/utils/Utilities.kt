package com.mcu.muzzchat.presentation.utils

import com.mcu.muzzchat.domain.models.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

sealed class MessageItem {
    data class Section(val text: String) : MessageItem()
    data class MessageGroup(
        val messages: List<Message>,
        val isFromCurrentUser: Boolean
    ) : MessageItem()
}

fun groupMessagesWithSections(messages: List<Message>): List<MessageItem> {
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
}

fun formatSectionHeader(timestamp: LocalDateTime): String {
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val day = timestamp.format(dayFormatter)
    val time = timestamp.format(timeFormatter)

    return "$day $time"
}