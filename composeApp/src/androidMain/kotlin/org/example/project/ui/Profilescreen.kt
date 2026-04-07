package org.example.project.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.meng
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen(
    profileName: String,
    nim: String,
    bio: String,
    email: String,
    phone: String,
    location: String,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var currentName by remember(profileName) { mutableStateOf(profileName) }
    var currentBio by remember(bio) { mutableStateOf(bio) }

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
                checked = isDarkMode,
                onCheckedChange = { onToggleDarkMode() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                EditProfileSection(
                    isDarkMode = isDarkMode,
                    currentName = currentName,
                    currentBio = currentBio,
                    onSave = { name, newBio ->
                        currentName = name
                        currentBio = newBio
                        isEditing = false
                    },
                    onCancel = {
                        isEditing = false
                    }
                )
            } else {
                ProfileHeader(
                    name = currentName,
                    nim = nim
                )

                Text(
                    text = currentBio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                InfoItem(
                    icon = "💌",
                    label = "Email",
                    value = email,
                    isDarkMode = isDarkMode,
                    lightColor = Color(0xFFE8F5E9),
                    darkColor = Color(0xFF2F4A38)
                )

                InfoItem(
                    icon = "📱",
                    label = "Phone",
                    value = phone,
                    isDarkMode = isDarkMode,
                    lightColor = Color(0xFFFFEBEE),
                    darkColor = Color(0xFF553740)
                )

                InfoItem(
                    icon = "📍",
                    label = "Location",
                    value = location,
                    isDarkMode = isDarkMode,
                    lightColor = Color(0xFFFFF3E0),
                    darkColor = Color(0xFF5A4A31)
                )

                HobbySection(
                    isDarkMode = isDarkMode
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { isEditing = true },
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
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun TopBarSection(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "My Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Personal card & lifestyle overview",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    nim: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(Res.drawable.meng),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = nim,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InfoItem(
    icon: String,
    label: String,
    value: String,
    isDarkMode: Boolean,
    lightColor: Color,
    darkColor: Color
) {
    val containerColor = if (isDarkMode) darkColor else lightColor
    val titleColor = if (isDarkMode) Color(0xFFF5F5F5) else Color(0xFF1F1F1F)
    val valueColor = if (isDarkMode) Color(0xFFD8D8D8) else Color(0xFF555555)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = titleColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = valueColor
                )
            }
        }
    }
}

@Composable
fun HobbySection(
    isDarkMode: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Hobbies",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HobbyChip(
                    icon = "🍔",
                    text = "Eating",
                    bgColor = if (isDarkMode) Color(0xFF5A4F39) else Color(0xFFF3E7CF),
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
                HobbyChip(
                    icon = "😴",
                    text = "Sleeping",
                    bgColor = if (isDarkMode) Color(0xFF435444) else Color(0xFFDDEBDD),
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HobbyChip(
                    icon = "🎬",
                    text = "Watching K-Drama",
                    bgColor = if (isDarkMode) Color(0xFF5A434B) else Color(0xFFF6E1E6),
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
                HobbyChip(
                    icon = "⭐",
                    text = "Fangirl",
                    bgColor = if (isDarkMode) Color(0xFF4D4860) else Color(0xFFE8E0F4),
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HobbyChip(
                    icon = "💸",
                    text = "Becoming Rich",
                    bgColor = if (isDarkMode) Color(0xFF5E5B3B) else Color(0xFFF5F0D7),
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
                HobbyChip(
                    icon = "💭",
                    text = "Daydreaming",
                    bgColor = if (isDarkMode) Color(0xFF40566D) else Color(0xFFDCEAF7),
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun HobbyChip(
    icon: String,
    text: String,
    bgColor: Color,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor = if (isDarkMode) Color(0xFFF1F1F1) else Color(0xFF454545)
    val iconBg = if (isDarkMode) Color(0xFF2F2F2F) else Color(0xFFF7F5F2)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    )
}