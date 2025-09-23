package com.mcu.muzzchat.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mcu.muzzchat.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Query("UPDATE messages SET isRead = 1 WHERE id = :messageId")
    suspend fun markMessageAsRead(messageId: Long)

    @Query("UPDATE messages SET isRead = 1 WHERE isFromCurrentUser = 0")
    suspend fun markAllMessagesAsRead()
}