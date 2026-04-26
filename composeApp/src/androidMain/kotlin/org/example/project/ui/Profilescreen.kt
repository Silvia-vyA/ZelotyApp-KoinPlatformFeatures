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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.meng
import org.jetbrains.compose.resources.painterResource
import android.os.Build
import androidx.compose.foundation.clickable
import org.example.project.platform.DeviceInfo
import org.koin.compose.koinInject

@Composable
fun SettingsScreenContent(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onBack: () -> Unit
) {
    val cardColor = if (isDarkMode) Color(0xFF242033) else Color(0xFFF6F0FF)
    val innerColor = if (isDarkMode) Color(0xFF2F2A40) else Color(0xFFFFFAF3)
    val textColor = if (isDarkMode) Color(0xFFF5F5F5) else Color(0xFF1F1F1F)
    val subTextColor = if (isDarkMode) Color(0xFFD8D8D8) else Color(0xFF555555)
    val deviceInfo: DeviceInfo = koinInject()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Settings ⚙️",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = innerColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Theme Mode 🌙",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = if (isDarkMode) "Dark Mode Active" else "Light Mode Active",
                            fontSize = 13.sp,
                            color = subTextColor
                        )
                    }

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onToggleDarkMode() }
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = innerColor)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Device Info 📱",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = "Device: ${deviceInfo.getManufacturer()} ${deviceInfo.getDeviceName()}",
                        fontSize = 14.sp,
                        color = subTextColor
                    )

                    Text(
                        text = "OS: ${deviceInfo.getOsVersion()}",
                        fontSize = 14.sp,
                        color = subTextColor
                    )
                }
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
}
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
    var showSettings by remember { mutableStateOf(false) }
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
                onOpenSettings = { showSettings = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                showSettings -> {
                    SettingsScreenContent(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = onToggleDarkMode,
                        onBack = { showSettings = false }
                    )
                }

                isEditing -> {
                    EditProfileSection(
                        isDarkMode = isDarkMode,
                        currentName = currentName,
                        currentBio = currentBio,
                        onSave = { name, newBio ->
                            currentName = name
                            currentBio = newBio
                            isEditing = false
                        },
                        onCancel = { isEditing = false }
                    )
                }

                else -> {
                    ProfileMainCard(
                        name = currentName,
                        nim = nim,
                        bio = currentBio,
                        isDarkMode = isDarkMode
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

                    HobbySection(isDarkMode = isDarkMode)

                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Edit Profile")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8E0F4)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Settings ⚙️",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F1F)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Theme Mode 🌙",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (isDarkMode) "Dark Mode Active" else "Light Mode Active",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onToggleDarkMode() }
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFAF3)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Device Info 📱",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Device: ${Build.MANUFACTURER} ${Build.MODEL}",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )

                    Text(
                        text = "OS: Android ${Build.VERSION.RELEASE}",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )
                }
            }
        }
    }
}
@Composable
fun ProfileMainCard(
    name: String,
    nim: String,
    bio: String,
    isDarkMode: Boolean

) {
    val cardColor = if (isDarkMode) Color(0xFF242033) else Color(0xFFF6F0FF)
    val innerColor = if (isDarkMode) Color(0xFF2F2A40) else Color(0xFFFFFAF3)
    val textColor = if (isDarkMode) Color(0xFFF5F5F5) else Color(0xFF1F1F1F)
    val subTextColor = if (isDarkMode) Color(0xFFD8D8D8) else Color(0xFF666666)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Image(
                painter = painterResource(Res.drawable.meng),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    text = nim,
                    fontSize = 14.sp,
                    color = subTextColor
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(innerColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Mahasiswa Informatika ITERA 💻",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = subTextColor
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = innerColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "About Me ✨",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Text(
                        text = bio,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = subTextColor
                    )
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF242033) else Color(0xFFF6F0FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Edit Your Profile",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
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
                    shape = RoundedCornerShape(18.dp)
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
    onOpenSettings: () -> Unit
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

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFE8E0F4))
                .padding(14.dp)
                .clickable { onOpenSettings() }
        ) {
            Text("⚙️", fontSize = 24.sp)
        }
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
            Text(text = icon, fontSize = 28.sp)

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
                HobbyChip("🍔", "Eating", if (isDarkMode) Color(0xFF5A4F39) else Color(0xFFF3E7CF), isDarkMode, Modifier.weight(1f))
                HobbyChip("😴", "Sleeping", if (isDarkMode) Color(0xFF435444) else Color(0xFFDDEBDD), isDarkMode, Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HobbyChip("🎬", "Watching K-Drama", if (isDarkMode) Color(0xFF5A434B) else Color(0xFFF6E1E6), isDarkMode, Modifier.weight(1f))
                HobbyChip("⭐", "Fangirl", if (isDarkMode) Color(0xFF4D4860) else Color(0xFFE8E0F4), isDarkMode, Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HobbyChip("💸", "Becoming Rich", if (isDarkMode) Color(0xFF5E5B3B) else Color(0xFFF5F0D7), isDarkMode, Modifier.weight(1f))
                HobbyChip("💭", "Daydreaming", if (isDarkMode) Color(0xFF40566D) else Color(0xFFDCEAF7), isDarkMode, Modifier.weight(1f))
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
            Text(text = icon, fontSize = 15.sp)
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