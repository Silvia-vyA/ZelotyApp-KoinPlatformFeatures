package org.example.project.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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