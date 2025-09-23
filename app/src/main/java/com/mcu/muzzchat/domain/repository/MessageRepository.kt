package com.mcu.muzzchat.domain.repository

import com.mcu.muzzchat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getAllMessages(): Flow<List<Message>>
    suspend fun insertMessage(message: Message)
    suspend fun deleteAllMessages()
    suspend fun markMessageAsRead(messageId: Long)
    suspend fun markAllMessagesAsRead()
}