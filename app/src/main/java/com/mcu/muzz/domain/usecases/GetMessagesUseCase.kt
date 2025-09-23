package com.mcu.muzz.domain.usecases

import com.mcu.muzz.domain.models.Message
import com.mcu.muzz.domain.models.MessageGroup
import com.mcu.muzz.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<List<MessageGroup>> {
        return messageRepository.getAllMessages().map { messages ->
            groupMessages(messages)
        }
    }

    private fun groupMessages(messages: List<Message>): List<MessageGroup> {
        if (messages.isEmpty()) return emptyList()

        val groups = mutableListOf<MessageGroup>()
        var currentGroup = mutableListOf<Message>()
        var lastMessage: Message? = null

        messages.forEach { message ->
            val shouldStartNewGroup = lastMessage?.let { last ->
                // New group if different sender or more than 20 seconds apart
                last.senderId != message.senderId ||
                        ChronoUnit.SECONDS.between(last.timestamp, message.timestamp) > 20
            } ?: true

            val shouldShowTimestamp = lastMessage?.let { last ->
                // Show timestamp if more than 1 hour apart or first message
                ChronoUnit.HOURS.between(last.timestamp, message.timestamp) >= 1
            } ?: true

            if (shouldStartNewGroup && currentGroup.isNotEmpty()) {
                groups.add(MessageGroup(messages = currentGroup.toList()))
                currentGroup.clear()
            }

            if (shouldShowTimestamp) {
                // Add timestamp group if needed
                if (currentGroup.isNotEmpty()) {
                    groups.add(MessageGroup(messages = currentGroup.toList()))
                    currentGroup.clear()
                }

                val timestampText = formatTimestamp(message.timestamp)
                groups.add(
                    MessageGroup(
                        messages = listOf(message),
                        timestamp = timestampText,
                        shouldShowTimestamp = true
                    )
                )
            } else {
                currentGroup.add(message)
            }

            lastMessage = message
        }

        if (currentGroup.isNotEmpty()) {
            groups.add(MessageGroup(messages = currentGroup.toList()))
        }

        return groups
    }

    private fun formatTimestamp(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val daysBetween = ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate())

        return when {
            daysBetween == 0L -> "Today ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            daysBetween == 1L -> "Yesterday ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            daysBetween < 7L -> dateTime.format(DateTimeFormatter.ofPattern("EEEE HH:mm"))
            else -> dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        }
    }
}