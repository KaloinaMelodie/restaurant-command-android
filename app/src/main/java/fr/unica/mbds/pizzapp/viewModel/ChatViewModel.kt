package fr.unica.mbds.pizzapp.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import fr.unica.mbds.pizzapp.model.ChatMessage

class ChatViewModel : ViewModel() {
    val messages = mutableStateListOf<ChatMessage>()


    fun addUserMessage(text: String) {
        messages.add(ChatMessage(isUser = true, text = text))
    }

    fun addBotMessage(text: String) {
        messages.add(ChatMessage(isUser = false, text = text))
    }

    fun clearChat() {
        messages.clear()
    }

}