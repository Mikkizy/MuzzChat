package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.models.Message
import com.mcu.muzzchat.domain.repository.MessageRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(content: String, senderId: String) {
        if (content.isNotBlank()) {
            messageRepository.sendMessage(content.trim(), senderId)
        }
    }
}