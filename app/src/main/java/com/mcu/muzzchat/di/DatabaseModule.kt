package com.mcu.muzzchat.di

import android.content.Context
import androidx.room.Room
import com.mcu.muzzchat.data.local.dao.MessageDao
import com.mcu.muzzchat.data.local.database.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext context: Context): MessageDatabase {
        return Room.databaseBuilder(
            context,
            MessageDatabase::class.java,
            "chat_database"
        ).build()
    }

    @Provides
    fun provideMessageDao(database: MessageDatabase): MessageDao {
        return database.messageDao()
    }
}