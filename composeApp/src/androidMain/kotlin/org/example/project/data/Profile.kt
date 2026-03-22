package org.example.project.data

data class Profile(
    val name: String,
    val nim: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String
)

data class ProfileUiState(
    val profile: Profile = Profile(
        name = "Silvia",
        nim = "123140133",
        bio = "Calon Ahli SYURGA yang baik lagipun Dermawan",
        email = "taktaulah04@gmail.com",
        phone = "+62 821 6941 0745",
        location = "Solok, Sumatra Barat"
    ),
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false
)