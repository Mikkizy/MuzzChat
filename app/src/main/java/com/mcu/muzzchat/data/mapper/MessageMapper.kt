package com.mcu.muzzchat.data.mapper

import com.mcu.muzzchat.data.local.entity.MessageEntity
import com.mcu.muzzchat.domain.models.Message
import java.time.LocalDateTime

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    text = text,
    timestamp = LocalDateTime.parse(timestamp),
    isFromCurrentUser = isFromCurrentUser
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    text = text,
    timestamp = timestamp.toString(),
    isFromCurrentUser = isFromCurrentUser
)
