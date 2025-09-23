package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.repository.MessageRepository
import javax.inject.Inject

class MarkMessagesAsReadUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(currentUserId: String) {
        messageRepository.markMessagesAsRead(currentUserId)
    }
}