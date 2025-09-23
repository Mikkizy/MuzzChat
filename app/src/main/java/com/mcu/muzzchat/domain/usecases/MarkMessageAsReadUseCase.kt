package com.mcu.muzzchat.domain.usecases

import com.mcu.muzzchat.domain.repository.MessageRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MarkMessagesAsReadUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(messageId: Long) {
        // Simulate read delay (realistic user behavior)
        delay(1000)
        repository.markMessageAsRead(messageId)
    }

    suspend fun markAllAsRead() {
        repository.markAllMessagesAsRead()
    }
}