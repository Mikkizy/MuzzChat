package com.mcu.muzz.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcu.muzz.data.local.converter.DateConverter
import com.mcu.muzz.data.local.dao.MessageDao
import com.mcu.muzz.data.local.dao.UserDao
import com.mcu.muzz.data.local.entity.MessageEntity
import com.mcu.muzz.data.local.entity.UserEntity

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