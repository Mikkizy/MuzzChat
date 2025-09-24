package com.mcu.muzz

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mcu.muzz.presentation.chat.ChatScreen
import com.mcu.muzz.presentation.chat.ChatViewModel
import com.mcu.muzz.presentation.ui.theme.MuzzChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        enableEdgeToEdge()
        setContent {
            MuzzChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val chatViewModel: ChatViewModel = hiltViewModel()
                    val uiState by chatViewModel.uiState.collectAsStateWithLifecycle()
                    val messageText by chatViewModel.messageText.collectAsStateWithLifecycle()
                    ChatScreen(
                        onBackClick = { finish() },
                        messageText = messageText,
                        uiState = uiState,
                        switchUser = chatViewModel::switchUser,
                        sendMessage = chatViewModel::sendMessage,
                        clearError = chatViewModel::clearError,
                        onMessageTextChanged = chatViewModel::onMessageTextChanged
                    )
                }
            }
        }
    }
}