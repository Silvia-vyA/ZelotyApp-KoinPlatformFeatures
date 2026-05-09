package org.example.project.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AIMessage(
    val text: String,
    val isUser: Boolean
)

data class AIUiState(
    val messages: List<AIMessage> = listOf(
        AIMessage(
            text = "Halo, aku Zeloty AI 🤖\nAku bisa bantu jawab pertanyaan, ringkas catatan, translate, atau buat ide judul.",
            isUser = false
        )
    ),
    val isLoading: Boolean = false
)

class AIViewModel(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AIUiState())
    val uiState: StateFlow<AIUiState> = _uiState.asStateFlow()

    fun sendMessage(message: String) {
        if (message.isBlank() || _uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                messages = it.messages + AIMessage(message, isUser = true),
                isLoading = true
            )
        }

        viewModelScope.launch {
            aiRepository.chat(message)
                .onSuccess { answer ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + AIMessage(answer, isUser = false),
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + AIMessage(
                                text = "Maaf, terjadi masalah: ${error.message}",
                                isUser = false
                            ),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun makeTitleFromNote(text: String) {
        sendMessage("Buatkan ide judul untuk catatan ini:\n$text")
    }

    fun summarizeNote(text: String) {
        sendMessage("Ringkas catatan ini:\n$text")
    }
    fun translateNote(text: String) {
        sendMessage("Translate catatan ini ke Bahasa Inggris:\n$text")
    }
}