package com.mcu.muzzchat.data.repository

import com.mcu.muzzchat.data.local.dao.MessageDao
import com.mcu.muzzchat.data.local.entity.MessageEntity
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {

    override fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAllMessages().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun sendMessage(content: String, senderId: String) {
        val message = MessageEntity(
            id = UUID.randomUUID().toString(),
            content = content,
            senderId = senderId,
            timestamp = LocalDateTime.now(),
            isRead = false
        )
        messageDao.insertMessage(message)
    }

    override suspend fun markMessagesAsRead(currentUserId: String) {
        messageDao.markMessagesAsRead(currentUserId)
    }

    private fun MessageEntity.toDomainModel(): Message {
        return Message(
            id = id,
            content = content,
            senderId = senderId,
            timestamp = timestamp,
            isRead = isRead
        )
    }
}