package com.mcu.muzz.di

import android.content.Context
import androidx.room.Room
import com.mcu.muzz.data.local.dao.MessageDao
import com.mcu.muzz.data.local.dao.UserDao
import com.mcu.muzz.data.local.database.ChatDatabase
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
    fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            ChatDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideMessageDao(database: ChatDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideUserDao(database: ChatDatabase): UserDao {
        return database.userDao()
    }
}