package com.mcu.muzzchat.domain.repository

import com.mcu.muzzchat.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserById(userId: String): User?
    suspend fun initializeUsers()
}