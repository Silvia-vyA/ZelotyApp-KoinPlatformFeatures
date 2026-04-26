package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.data.NoteRepository
import org.example.project.data.SettingsManager

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false
)

data class Profile(
    val name: String,
    val nim: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String
)

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val profile: Profile = Profile(
        name = "Silvia",
        nim = "123140133",
        bio = "Mahasiswa Informatika ITERA",
        email = "taktaulah04@gmail.com",
        phone = "+62 821 6941 0745",
        location = "Solok, Sumatra Barat"
    ),
    val isDarkMode: Boolean = false,
    val sortOrder: String = "newest",
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NotesUiState(
            isDarkMode = settingsManager.isDarkMode,
            sortOrder = settingsManager.sortOrder
        )
    )

    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private var notesJob: Job? = null

    init {
        loadNotes()
    }

    fun loadNotes() {
        notesJob?.cancel()

        notesJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getNotes(_uiState.value.sortOrder).collect { notes ->
                _uiState.value = _uiState.value.copy(
                    notes = notes,
                    isLoading = false
                )
            }
        }
    }

    fun searchNotes(query: String) {
        notesJob?.cancel()

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            isLoading = true
        )

        notesJob = viewModelScope.launch {
            if (query.isBlank()) {
                repository.getNotes(_uiState.value.sortOrder).collect { notes ->
                    _uiState.value = _uiState.value.copy(
                        notes = notes,
                        isLoading = false
                    )
                }
            } else {
                repository.searchNotes(query).collect { notes ->
                    _uiState.value = _uiState.value.copy(
                        notes = notes,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun changeSortOrder(order: String) {
        settingsManager.sortOrder = order

        _uiState.value = _uiState.value.copy(
            sortOrder = order
        )

        loadNotes()
    }

    fun toggleDarkMode() {
        val newValue = !_uiState.value.isDarkMode
        settingsManager.isDarkMode = newValue

        _uiState.value = _uiState.value.copy(
            isDarkMode = newValue
        )
    }

    fun getNoteById(noteId: Int): Note? {
        return _uiState.value.notes.find { it.id == noteId }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(title, content)
            loadNotes()
        }
    }

    fun updateNote(noteId: Int, title: String, content: String) {
        viewModelScope.launch {
            repository.updateNote(noteId, title, content)
            loadNotes()
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
            loadNotes()
        }
    }

    fun addToFavorite(noteId: Int) {
        viewModelScope.launch {
            repository.addToFavorite(noteId)
            loadNotes()
        }
    }

    fun removeFromFavorite(noteId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorite(noteId)
            loadNotes()
        }
    }
}