package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.repository.MessageRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(text: String, isFromCurrentUser: Boolean) {
        val message = Message(
            text = text,
            timestamp = LocalDateTime.now(),
            isFromCurrentUser = isFromCurrentUser
        )
        repository.insertMessage(message)
    }
}