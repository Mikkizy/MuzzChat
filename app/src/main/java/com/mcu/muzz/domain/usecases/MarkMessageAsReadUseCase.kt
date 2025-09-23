package com.mcu.muzz.domain.usecases

import com.mcu.muzz.domain.repository.MessageRepository
import javax.inject.Inject

class MarkMessagesAsReadUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(currentUserId: String) {
        messageRepository.markMessagesAsRead(currentUserId)
    }
}