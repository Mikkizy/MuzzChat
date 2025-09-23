package com.mcu.muzzchat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcu.muzzchat.data.local.converter.DateConverter
import com.mcu.muzzchat.data.local.dao.MessageDao
import com.mcu.muzzchat.data.local.dao.UserDao
import com.mcu.muzzchat.data.local.entity.MessageEntity
import com.mcu.muzzchat.data.local.entity.UserEntity

@Database(
    entities = [MessageEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "chat_database"
    }
}