package com.mcu.muzz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mcu.muzz.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM chats ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("UPDATE chats SET isRead = 1 WHERE senderId != :currentUserId AND isRead = 0")
    suspend fun markMessagesAsRead(currentUserId: String)

    @Query("DELETE FROM chats")
    suspend fun deleteAllMessages()
}