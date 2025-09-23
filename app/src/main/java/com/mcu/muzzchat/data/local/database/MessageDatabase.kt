package com.mcu.muzzchat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcu.muzzchat.data.local.converter.LocalDateTimeConverter
import com.mcu.muzzchat.data.local.dao.MessageDao
import com.mcu.muzzchat.data.local.entity.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}