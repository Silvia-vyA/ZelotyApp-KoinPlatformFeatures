package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.ui.components.HobbySection
import org.example.project.ui.components.InfoItem
import org.example.project.ui.components.LabeledTextField
import org.example.project.ui.components.ProfileHeader
import org.example.project.ui.components.TopBarSection
import org.example.project.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarSection(
                checked = uiState.isDarkMode,
                onCheckedChange = { viewModel.toggleDarkMode() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isEditing) {
                EditProfileSection(
                    isDarkMode = uiState.isDarkMode,
                    currentName = profile.name,
                    currentBio = profile.bio,
                    onSave = { name, bio ->
                        viewModel.saveProfile(name, bio)
                    },
                    onCancel = {
                        viewModel.cancelEdit()
                    }
                )
            } else {
                ProfileHeader(
                    name = profile.name,
                    nim = profile.nim
                )

                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                InfoItem(
                    icon = "💌",
                    label = "Email",
                    value = profile.email,
                    isDarkMode = uiState.isDarkMode,
                    lightColor = Color(0xFFE8F5E9),
                    darkColor = Color(0xFF2F4A38)
                )

                InfoItem(
                    icon = "📱",
                    label = "Phone",
                    value = profile.phone,
                    isDarkMode = uiState.isDarkMode,
                    lightColor = Color(0xFFFFEBEE),
                    darkColor = Color(0xFF553740)
                )

                InfoItem(
                    icon = "📍",
                    label = "Location",
                    value = profile.location,
                    isDarkMode = uiState.isDarkMode,
                    lightColor = Color(0xFFFFF3E0),
                    darkColor = Color(0xFF5A4A31)
                )

                HobbySection(
                    isDarkMode = uiState.isDarkMode
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.startEdit() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Edit Profile")
                }
            }
        }
    }
}

@Composable
fun EditProfileSection(
    isDarkMode: Boolean,
    currentName: String,
    currentBio: String,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var bio by remember { mutableStateOf(currentBio) }

    val gradient = if (isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1E1B2E),
                Color(0xFF151515)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF6F0FF),
                Color(0xFFFFF8F1)
            )
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Edit Your Profile",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Update your display name and short bio to match your vibe.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LabeledTextField(
                label = "Display Name",
                value = name,
                onValueChange = { name = it }
            )

            LabeledTextField(
                label = "Bio",
                value = bio,
                onValueChange = { bio = it }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onSave(name, bio) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Save")
                }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}