package org.example.project.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.db.NotesDatabase
import org.example.project.viewmodel.Note

class NoteRepository(
    driverFactory: DatabaseDriverFactory
) {
    private val database = NotesDatabase(driverFactory.createDriver())
    private val queries = database.noteQueries

    fun getNotes(sortOrder: String): Flow<List<Note>> {
        val query = if (sortOrder == "oldest") {
            queries.selectAllOldest()
        } else {
            queries.selectAllNewest()
        }

        return query.asFlow()
            .mapToList(Dispatchers.Default)
            .map { list ->
                list.map { entity ->
                    Note(
                        id = entity.id.toInt(),
                        title = entity.title,
                        content = entity.content,
                        isFavorite = entity.is_favorite == 1L
                    )
                }
            }
    }

    fun searchNotes(keyword: String): Flow<List<Note>> {
        val search = "%$keyword%"

        return queries.searchNotes(search, search)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list ->
                list.map { entity ->
                    Note(
                        id = entity.id.toInt(),
                        title = entity.title,
                        content = entity.content,
                        isFavorite = entity.is_favorite == 1L
                    )
                }
            }
    }

    suspend fun insertNote(title: String, content: String) {
        withContext(Dispatchers.Default) {
            queries.insertNote(
                title = title,
                content = content,
                created_at = System.currentTimeMillis()
            )
        }
    }

    suspend fun updateNote(id: Int, title: String, content: String) {
        withContext(Dispatchers.Default) {
            queries.updateNote(
                title = title,
                content = content,
                id = id.toLong()
            )
        }
    }

    suspend fun deleteNote(id: Int) {
        withContext(Dispatchers.Default) {
            queries.deleteNote(id.toLong())
        }
    }

    suspend fun addToFavorite(id: Int) {
        withContext(Dispatchers.Default) {
            queries.addToFavorite(id.toLong())
        }
    }

    suspend fun removeFromFavorite(id: Int) {
        withContext(Dispatchers.Default) {
            queries.removeFromFavorite(id.toLong())
        }
    }
}