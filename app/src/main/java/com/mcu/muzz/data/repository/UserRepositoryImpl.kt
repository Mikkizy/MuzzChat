package com.mcu.muzz.data.repository

import com.mcu.muzz.data.local.dao.UserDao
import com.mcu.muzz.data.local.entity.UserEntity
import com.mcu.muzz.domain.models.User
import com.mcu.muzz.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)?.toDomainModel()
    }

    override suspend fun initializeUsers() {
        val users = listOf(
            UserEntity(id = "user1", name = "Sarah"),
            UserEntity(id = "user2", name = "John")
        )
        userDao.insertUsers(users)
    }

    private fun UserEntity.toDomainModel(): User {
        return User(
            id = id,
            name = name,
            avatarUrl = avatarUrl
        )
    }
}