package org.example.project.navigation

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val emoji: String
) {
    object Notes : BottomNavItem(Screen.Notes.route, "Notes", "📝")
    object Favorites : BottomNavItem(Screen.Favorites.route, "Favorites", "⭐")
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", "👤")
}