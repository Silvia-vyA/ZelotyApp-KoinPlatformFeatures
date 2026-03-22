package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.data.ProfileUiState

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleDarkMode() {
        _uiState.update { current ->
            current.copy(isDarkMode = !current.isDarkMode)
        }
    }

    fun startEdit() {
        _uiState.update { it.copy(isEditing = true) }
    }

    fun cancelEdit() {
        _uiState.update { it.copy(isEditing = false) }
    }

    fun saveProfile(name: String, bio: String) {
        _uiState.update { current ->
            current.copy(
                profile = current.profile.copy(
                    name = name,
                    bio = bio
                ),
                isEditing = false
            )
        }
    }
}