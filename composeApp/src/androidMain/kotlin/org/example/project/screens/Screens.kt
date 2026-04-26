package org.example.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.viewmodel.Note
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import org.example.project.platform.NetworkMonitor
import org.koin.compose.koinInject

private val LightPastelColors = listOf(
    Color(0xFFE8F5E9), // mint
    Color(0xFFFFEBEE), // soft pink
    Color(0xFFFFF3E0), // peach
    Color(0xFFE8E0F4), // lavender
    Color(0xFFE3F2FD), // baby blue
    Color(0xFFE0F7FA), // cyan
    Color(0xFFFCE4EC), // rose
    Color(0xFFFFFDE7), // soft yellow
    Color(0xFFF1F8E9), // lime soft
    Color(0xFFEDE7F6)  // purple soft
)

private val DarkPastelColors = listOf(
    Color(0xFF2F4A38), // dark mint
    Color(0xFF553740), // dark pink
    Color(0xFF5A4A31), // dark peach
    Color(0xFF4D4860), // dark lavender
    Color(0xFF30485C), // dark blue
    Color(0xFF27535A), // dark cyan
    Color(0xFF5A3A4A), // dark rose
    Color(0xFF5A5635), // dark yellow
    Color(0xFF3E5235), // dark lime
    Color(0xFF46395A)  // dark purple
)
@Composable
fun NetworkStatusIndicator() {
    val networkMonitor: NetworkMonitor = koinInject()
    val isConnected by networkMonitor.observeConnectivity().collectAsState(
        initial = networkMonitor.isConnected()
    )

    if (!isConnected) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFB71C1C),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = "No Internet Connection ❌",
                color = Color.White,
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun isDarkModeNow(): Boolean {
    return MaterialTheme.colorScheme.background.luminance() < 0.5f
}

@Composable
private fun noteCardColor(index: Int): Color {
    val isDark = isDarkModeNow()
    return if (isDark) {
        DarkPastelColors[index % DarkPastelColors.size]
    } else {
        LightPastelColors[index % LightPastelColors.size]
    }
}

@Composable
private fun softSurfaceColor(): Color {
    val isDark = isDarkModeNow()
    return if (isDark) Color(0xFF1E1A2B) else Color(0xFFF6F0FF)
}

@Composable
private fun innerSurfaceColor(): Color {
    val isDark = isDarkModeNow()
    return if (isDark) Color(0xFF2A2438) else Color(0xFFFFFAF3)
}

@Composable
fun NoteListScreen(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredNotes = notes.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = softSurfaceColor(),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(horizontal = 16.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Zeloty ✨",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Your soft space to save dreams, plans, and little thoughts.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    title = "📝 Notes",
                    value = "${notes.size} saved",
                    color = noteCardColor(2),
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "⭐ Favorite",
                    value = "${notes.count { it.isFavorite }} marked",
                    color = noteCardColor(3),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search note...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
        NetworkStatusIndicator()
        Spacer(modifier = Modifier.height(18.dp))

        when {
            notes.isEmpty() -> EmptyMessage("Belum ada notes. Tekan + untuk menambahkan catatan.")
            filteredNotes.isEmpty() -> EmptyMessage("Note tidak ditemukan 🔍")
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(filteredNotes) { index, note ->
                    PrettyNoteCard(
                        note = note,
                        index = index,
                        onClick = { onNoteClick(note.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NotesHeader(
            title = "Favorite Notes",
            subtitle = "Your sweetest saved notes."
        )

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = softSurfaceColor()),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("⭐", fontSize = 34.sp)
                        Text(
                            text = "Belum ada note favorit",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tekan tombol favorite di detail note dulu ya.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(notes) { index, note ->
                    PrettyNoteCard(
                        note = note,
                        index = index,
                        onClick = { onNoteClick(note.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotesHeader(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun PrettyNoteCard(
    note: Note,
    index: Int = note.id,
    onClick: () -> Unit
) {
    val isDark = isDarkModeNow()
    val bgColor = noteCardColor(index)
    val titleColor = if (isDark) Color(0xFFF8F5FF) else Color(0xFF1F1F1F)
    val contentColor = if (isDark) Color(0xFFE2DDEA) else Color(0xFF555555)
    val smallTextColor = if (isDark) Color(0xFFCFC8DA) else Color(0xFF777777)
    val circleColor = if (isDark) Color(0xFF211D2B) else Color(0xFFFDFBFF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .background(circleColor, CircleShape)
                    .padding(10.dp)
            ) {
                Text(
                    text = if (note.isFavorite) "⭐" else "📝",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = titleColor
                )

                Text(
                    text = note.content,
                    fontSize = 14.sp,
                    color = contentColor
                )

                Text(
                    text = if (note.isFavorite) "Favorite Note" else "Tap to see details",
                    fontSize = 12.sp,
                    color = smallTextColor
                )
            }
        }
    }
}

@Composable
fun AddNoteScreen(
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        NotesHeader(
            title = "Add Note",
            subtitle = "Create a new pretty note."
        )

        EditInputCard(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it }
        )

        Button(
            onClick = { onSave(title, content) },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Save")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(22.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun NoteDetailScreen(
    note: Note,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val isDark = isDarkModeNow()
    val bgColor = noteCardColor(note.id)
    val titleColor = if (isDark) Color(0xFFF8F5FF) else Color(0xFF1F1F1F)
    val textColor = if (isDark) Color(0xFFE2DDEA) else Color(0xFF444444)
    val smallColor = if (isDark) Color(0xFFCFC8DA) else Color(0xFF666666)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        NotesHeader(
            title = "Note Detail",
            subtitle = "See your note more clearly."
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = note.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )

                Text(
                    text = "ID Note: ${note.id}",
                    fontSize = 13.sp,
                    color = smallColor
                )

                Text(
                    text = note.content,
                    fontSize = 15.sp,
                    color = textColor
                )

                Text(
                    text = if (note.isFavorite) "Status: Favorite" else "Status: Not Favorite",
                    fontSize = 13.sp,
                    color = smallColor
                )
            }
        }

        Button(
            onClick = onEdit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Edit Note")
        }

        Button(
            onClick = onToggleFavorite,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = if (note.isFavorite) {
                    "Remove from Favorite ⭐"
                } else {
                    "Add to Favorite ⭐"
                }
            )
        }

        Button(
            onClick = onDelete,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDark) Color(0xFF6D2E38) else Color(0xFFFFCDD2),
                contentColor = if (isDark) Color(0xFFFFE8EC) else Color(0xFFB71C1C)
            )
        ) {
            Text("Delete Note")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun EditNoteScreen(
    note: Note,
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        NotesHeader(
            title = "Edit Note",
            subtitle = "Polish your note beautifully."
        )

        EditInputCard(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            noteId = note.id
        )

        Button(
            onClick = { onSave(title, content) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Update")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun EditInputCard(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    noteId: Int? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = softSurfaceColor()),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (noteId != null) {
                Text(
                    text = "Editing Note ID: $noteId",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}