package com.mcu.muzz.domain.repository

import com.mcu.muzz.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserById(userId: String): User?
    suspend fun initializeUsers()
}