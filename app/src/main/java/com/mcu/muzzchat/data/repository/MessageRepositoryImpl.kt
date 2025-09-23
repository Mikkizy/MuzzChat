package com.mcu.muzzchat.data.repository

import com.mcu.muzzchat.data.local.dao.MessageDao
import com.mcu.muzzchat.data.mapper.toDomain
import com.mcu.muzzchat.data.mapper.toEntity
import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {

    override fun getAllMessages(): Flow<List<Message>> =
        messageDao.getAllMessages().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message.toEntity())
    }

    override suspend fun deleteAllMessages() {
        messageDao.deleteAllMessages()
    }
}