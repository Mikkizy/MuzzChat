package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke(): Flow<List<Message>> = repository.getAllMessages()
}