package com.mcu.muzzchat

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
import com.mcu.muzzchat.presentation.chat.ChatScreen
import com.mcu.muzzchat.presentation.chat.ChatViewModel
import com.mcu.muzzchat.presentation.ui.theme.MuzzChatTheme
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
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

                    ChatScreen(
                        onBackClick = { finish() },
                        uiState = uiState,
                        updateCurrentMessage = chatViewModel::updateCurrentMessage,
                        sendMessage = chatViewModel::sendMessage
                    )
                }
            }
        }
    }
}