package com.mcu.muzz.data.local.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mcu.muzz.data.local.database.ChatDatabase
import com.mcu.muzz.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class MessageDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ChatDatabase
    private lateinit var messageDao: MessageDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ChatDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        messageDao = database.messageDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertMessage_and_getAllMessages() = runTest {
        // Given
        val message = MessageEntity(
            id = "1",
            content = "Test message",
            senderId = "user1",
            timestamp = LocalDateTime.now(),
            isRead = false
        )

        // When
        messageDao.insertMessage(message)
        val messages = messageDao.getAllMessages().first()

        // Then
        assertEquals(1, messages.size)
        assertEquals(message.id, messages[0].id)
        assertEquals(message.content, messages[0].content)
        assertEquals(message.senderId, messages[0].senderId)
        assertFalse(messages[0].isRead)
    }

    @Test
    fun markMessagesAsRead_updatesCorrectMessages() = runTest {
        // Given
        val message1 = MessageEntity("1", "From user2", "user2", LocalDateTime.now(), false)
        val message2 = MessageEntity("2", "From user1", "user1", LocalDateTime.now(), false)

        messageDao.insertMessage(message1)
        messageDao.insertMessage(message2)

        // When
        messageDao.markMessagesAsRead("user1")

        // Then
        val messages = messageDao.getAllMessages().first()
        val user1Messages = messages.filter { it.senderId == "user1" }
        val user2Messages = messages.filter { it.senderId == "user2" }

        assertTrue("Messages from user2 should be read", user2Messages.all { it.isRead })
        assertTrue("Messages from user1 should remain unread", user1Messages.all { !it.isRead })
    }
}