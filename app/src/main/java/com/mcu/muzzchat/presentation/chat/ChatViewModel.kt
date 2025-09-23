package com.mcu.muzzchat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcu.muzzchat.domain.usecases.GenerateAutoReplyUseCase
import com.mcu.muzzchat.domain.usecases.GetMessagesUseCase
import com.mcu.muzzchat.domain.usecases.MarkMessagesAsReadUseCase
import com.mcu.muzzchat.domain.usecases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val generateAutoReplyUseCase: GenerateAutoReplyUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
        markUnreadMessagesAsRead()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            getMessagesUseCase().collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    private fun markUnreadMessagesAsRead() {
        viewModelScope.launch {
            // Mark all other user's messages as read when app opens
            markMessagesAsReadUseCase.markAllAsRead()
        }
    }

    fun updateCurrentMessage(message: String) {
        _uiState.value = _uiState.value.copy(currentMessage = message)
    }

    fun sendMessage() {
        val currentMessage = _uiState.value.currentMessage.trim()
        if (currentMessage.isBlank()) return

        viewModelScope.launch {
            // Send user message (starts as unread)
            sendMessageUseCase(currentMessage, true)
            _uiState.value = _uiState.value.copy(currentMessage = "")

            // Generate auto-reply
            generateAutoReplyUseCase(currentMessage)?.let { reply ->
                sendMessageUseCase(reply, false)

                // Simulate the other user reading the current user's message
                // Find the latest message from current user and mark as read
                val latestUserMessage = _uiState.value.messages
                    .filter { it.isFromCurrentUser }
                    .maxByOrNull { it.timestamp }

                latestUserMessage?.let { message ->
                    markMessagesAsReadUseCase(message.id)
                }
            }
        }
    }

    fun sendOtherUserMessage(message: String) {
        if (message.isBlank()) return

        viewModelScope.launch {
            sendMessageUseCase(message, false)
            // Auto-mark other user messages as read after a delay
            markMessagesAsReadUseCase.markAllAsRead()
        }
    }

    fun markMessageAsRead(messageId: Long) {
        viewModelScope.launch {
            markMessagesAsReadUseCase(messageId)
        }
    }
}