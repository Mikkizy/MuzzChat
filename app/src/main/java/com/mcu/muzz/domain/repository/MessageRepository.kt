package com.mcu.muzz.domain.repository

import com.mcu.muzz.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getAllMessages(): Flow<List<Message>>
    suspend fun sendMessage(content: String, senderId: String)
    suspend fun markMessagesAsRead(currentUserId: String)
}