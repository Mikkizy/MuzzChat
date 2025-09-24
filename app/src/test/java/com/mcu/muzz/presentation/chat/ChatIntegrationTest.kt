package com.mcu.muzz.presentation.chat

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mcu.muzz.data.local.database.ChatDatabase
import com.mcu.muzz.data.local.entity.MessageEntity
import com.mcu.muzz.data.local.entity.UserEntity
import com.mcu.muzz.data.repository.MessageRepositoryImpl
import com.mcu.muzz.data.repository.UserRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class ChatIntegrationTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ChatDatabase
    private lateinit var messageRepository: MessageRepositoryImpl
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ChatDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        messageRepository = MessageRepositoryImpl(database.messageDao())
        userRepository = UserRepositoryImpl(database.userDao())
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `message repository should persist and retrieve messages`() = runTest {
        // Given
        val content = "Test message"
        val senderId = "user1"

        // When
        messageRepository.sendMessage(content, senderId)

        // Then
        val messages = messageRepository.getAllMessages().first()
        assertEquals(1, messages.size)
        assertEquals(content, messages[0].content)
        assertEquals(senderId, messages[0].senderId)
        assertFalse(messages[0].isRead)
    }

    @Test
    fun `message repository should mark messages as read`() = runTest {
        // Given
        val messageEntity1 = MessageEntity(
            id = "1",
            content = "Hello from user2",
            senderId = "user2",
            timestamp = LocalDateTime.now(),
            isRead = false
        )
        val messageEntity2 = MessageEntity(
            id = "2",
            content = "Hello from user1",
            senderId = "user1",
            timestamp = LocalDateTime.now(),
            isRead = false
        )
        database.messageDao().insertMessage(messageEntity1)
        database.messageDao().insertMessage(messageEntity2)

        // When
        messageRepository.markMessagesAsRead("user1")

        // Then
        val messages = messageRepository.getAllMessages().first()
        val user2Message = messages.find { it.senderId == "user2" }
        val user1Message = messages.find { it.senderId == "user1" }

        assertTrue("Message from user2 should be marked as read", user2Message!!.isRead)
        assertFalse("Message from user1 should remain unread", user1Message!!.isRead)
    }

    @Test
    fun `user repository should initialize and retrieve users`() = runTest {
        // When
        userRepository.initializeUsers()

        // Then
        val users = userRepository.getAllUsers().first()
        assertEquals(2, users.size)
        assertEquals("Sarah", users.find { it.id == "user1" }?.name)
        assertEquals("Miracle", users.find { it.id == "user2" }?.name)
    }

    @Test
    fun `user repository should find user by id`() = runTest {
        // Given
        val userEntity = UserEntity("test_user", "Test User", null)
        database.userDao().insertUser(userEntity)

        // When
        val user = userRepository.getUserById("test_user")

        // Then
        assertNotNull(user)
        assertEquals("test_user", user?.id)
        assertEquals("Test User", user?.name)
    }

    @Test
    fun `user repository should return null for non-existent user`() = runTest {
        // When
        val user = userRepository.getUserById("non_existent")

        // Then
        assertNull(user)
    }

    @Test
    fun `messages should be ordered by timestamp`() = runTest {
        // Given
        val baseTime = LocalDateTime.now()
        val message1 = MessageEntity("1", "First", "user1", baseTime, false)
        val message2 = MessageEntity("2", "Second", "user1", baseTime.plusMinutes(1), false)
        val message3 = MessageEntity("3", "Third", "user1", baseTime.minusMinutes(1), false)

        database.messageDao().insertMessage(message1)
        database.messageDao().insertMessage(message2)
        database.messageDao().insertMessage(message3)

        // When
        val messages = messageRepository.getAllMessages().first()

        // Then
        assertEquals(3, messages.size)
        assertEquals("Third", messages[0].content)  // Earliest
        assertEquals("First", messages[1].content)  // Middle
        assertEquals("Second", messages[2].content) // Latest
    }
}