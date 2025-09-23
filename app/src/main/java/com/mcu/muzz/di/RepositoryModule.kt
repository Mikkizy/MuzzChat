package com.mcu.muzz.di

import com.mcu.muzz.data.repository.MessageRepositoryImpl
import com.mcu.muzz.data.repository.UserRepositoryImpl
import com.mcu.muzz.domain.repository.MessageRepository
import com.mcu.muzz.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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