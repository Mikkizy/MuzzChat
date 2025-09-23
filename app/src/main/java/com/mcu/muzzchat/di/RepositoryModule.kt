package com.mcu.muzzchat.di

import com.mcu.muzzchat.data.repository.MessageRepositoryImpl
import com.mcu.muzzchat.data.repository.UserRepositoryImpl
import com.mcu.muzzchat.domain.repository.MessageRepository
import com.mcu.muzzchat.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}