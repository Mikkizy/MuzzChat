package com.mcu.muzz.domain.usecases

import com.mcu.muzz.domain.repository.MessageRepository
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