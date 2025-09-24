package com.mcu.muzzchat.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mcu.muzzchat.data.local.database.MessageDatabase
import com.mcu.muzzchat.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class MessageDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MessageDatabase
    private lateinit var messageDao: MessageDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MessageDatabase::class.java
        ).allowMainThreadQueries().build()

        messageDao = database.messageDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertMessage_andGetAllMessages_returnsCorrectOrder() = runTest {
        // Given
        val message1 = MessageEntity(
            text = "First message",
            timestamp = "2023-01-01T10:00:00",
            isFromCurrentUser = true
        )
        val message2 = MessageEntity(
            text = "Second message",
            timestamp = "2023-01-01T11:00:00",
            isFromCurrentUser = false
        )

        // When
        messageDao.insertMessage(message1)
        messageDao.insertMessage(message2)

        val messages = messageDao.getAllMessages().first()

        // Then
        assert(messages.size == 2)
        assert(messages[0].text == "First message")
        assert(messages[1].text == "Second message")
    }

    @Test
    fun deleteAllMessages_removesAllMessages() = runTest {
        // Given
        val message = MessageEntity(
            text = "Test message",
            timestamp = LocalDateTime.now().toString(),
            isFromCurrentUser = true
        )
        messageDao.insertMessage(message)

        // When
        messageDao.deleteAllMessages()
        val messages = messageDao.getAllMessages().first()

        // Then
        assert(messages.isEmpty())
    }
}