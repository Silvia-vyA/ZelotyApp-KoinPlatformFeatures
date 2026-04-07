package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    val notes: List<Note> = listOf(
        Note(
            id = 1,
            title = "Belajar Navigation Compose",
            content = "Pahami NavHost, NavController, routes, dan passing argument noteId.",
            isFavorite = true
        ),
        Note(
            id = 2,
            title = "Tugas Minggu 5",
            content = "Bottom nav, detail note, add note, edit note, dan back navigation.",
            isFavorite = false
        ),
        Note(
            id = 3,
            title = "Belanja",
            content = "Beli kopi, susu, dan roti.",
            isFavorite = true
        )
    ),
    val profile: Profile = Profile(
        name = "Silvia",
        nim = "123140133",
        bio = "Mahasiswa Informatika ITERA",
        email = "taktaulah04@gmail.com",
        phone = "+62 821 6941 0745",
        location = "Solok, Sumatra Barat"
    ),
    val isDarkMode: Boolean = false
)

class NotesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun getNoteById(noteId: Int): Note? {
        return _uiState.value.notes.find { it.id == noteId }
    }

    fun addNote(title: String, content: String) {
        _uiState.update { current ->
            val nextId = (current.notes.maxOfOrNull { it.id } ?: 0) + 1
            current.copy(
                notes = current.notes + Note(
                    id = nextId,
                    title = title,
                    content = content
                )
            )
        }
    }

    fun updateNote(noteId: Int, title: String, content: String) {
        _uiState.update { current ->
            current.copy(
                notes = current.notes.map { note ->
                    if (note.id == noteId) {
                        note.copy(title = title, content = content)
                    } else note
                }
            )
        }
    }

    fun toggleFavorite(noteId: Int) {
        _uiState.update { current ->
            current.copy(
                notes = current.notes.map { note ->
                    if (note.id == noteId) {
                        note.copy(isFavorite = !note.isFavorite)
                    } else note
                }
            )
        }
    }
}