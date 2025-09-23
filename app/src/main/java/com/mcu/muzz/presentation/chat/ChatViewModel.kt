package com.mcu.muzz.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcu.muzz.domain.repository.UserRepository
import com.mcu.muzz.domain.usecases.GetMessagesUseCase
import com.mcu.muzz.domain.usecases.MarkMessagesAsReadUseCase
import com.mcu.muzz.domain.usecases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUserId = MutableStateFlow("user1")
    private val _messageText = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val messageText: StateFlow<String> = _messageText.asStateFlow()

    val uiState: StateFlow<ChatUIState> = combine(
        getMessagesUseCase(),
        userRepository.getAllUsers(),
        _currentUserId,
        _isLoading,
        _error
    ) { messages, users, currentUserId, isLoading, error ->
        val currentUser = users.find { it.id == currentUserId }
        val otherUser = users.find { it.id != currentUserId }

        ChatUIState(
            messages = messages,
            currentUser = currentUser,
            otherUser = otherUser,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatUIState()
    )

    init {
        initializeUsers()
        markMessagesAsRead()
    }

    fun onMessageTextChanged(text: String) {
        _messageText.value = text
    }

    fun sendMessage() {
        val content = _messageText.value
        val senderId = _currentUserId.value

        if (content.isNotBlank()) {
            viewModelScope.launch {
                try {
                    sendMessageUseCase(content, senderId)
                    _messageText.value = ""
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = e.localizedMessage
                }
            }
        }
    }

    fun switchUser() {
        val currentUser = _currentUserId.value
        _currentUserId.value = if (currentUser == "user1") "user2" else "user1"
        markMessagesAsRead()
    }

    fun clearError() {
        _error.value = null
    }

    private fun initializeUsers() {
        viewModelScope.launch {
            try {
                userRepository.initializeUsers()
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }

    private fun markMessagesAsRead() {
        viewModelScope.launch {
            try {
                markMessagesAsReadUseCase(_currentUserId.value)
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }
}