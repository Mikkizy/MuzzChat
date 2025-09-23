package com.mcu.muzzchat.domain.usecases

import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class GenerateAutoReplyUseCase @Inject constructor() {

    private val replies = listOf(
        "That's interesting!",
        "I see what you mean",
        "Really?",
        "Tell me more about that",
        "That sounds great!",
        "I agree",
        "That's a good point",
        "How exciting!",
        "I hadn't thought of it that way",
        "That makes sense"
    )

    suspend operator fun invoke(originalMessage: String): String? {
        // 70% chance of auto-reply
        if (Random.nextFloat() > 0.7f) return null

        // Random delay between 1-3 seconds
        delay(Random.nextLong(1000, 3000))

        return replies.random()
    }
}