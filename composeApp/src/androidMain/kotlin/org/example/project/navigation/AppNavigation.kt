package org.example.project.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.example.project.screens.AddNoteScreen
import org.example.project.screens.EditNoteScreen
import org.example.project.screens.FavoritesScreen
import org.example.project.screens.NoteDetailScreen
import org.example.project.screens.NoteListScreen
import org.example.project.ui.ProfileScreen
import org.example.project.viewmodel.NotesViewModel

@Composable
fun AppNavigation(viewModel: NotesViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomItems = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text(item.emoji) },
                        label = { Text(item.label) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Notes.route) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddNote.route) }
                ) {
                    Text("+")
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Notes.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Notes.route) {
                NoteListScreen(
                    notes = uiState.notes,
                    onNoteClick = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    notes = uiState.notes.filter { it.isFavorite },
                    onNoteClick = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    profileName = uiState.profile.name,
                    nim = uiState.profile.nim,
                    bio = uiState.profile.bio,
                    email = uiState.profile.email,
                    phone = uiState.profile.phone,
                    location = uiState.profile.location,
                    isDarkMode = uiState.isDarkMode,
                    onToggleDarkMode = { viewModel.toggleDarkMode() }
                )
            }

            composable(Screen.AddNote.route) {
                AddNoteScreen(
                    onSave = { title, content ->
                        viewModel.addNote(title, content)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.NoteDetail.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                val note = viewModel.getNoteById(noteId)

                note?.let {
                    NoteDetailScreen(
                        note = it,
                        onBack = { navController.popBackStack() },
                        onEdit = {
                            navController.navigate(Screen.EditNote.createRoute(noteId))
                        },
                        onToggleFavorite = {
                            viewModel.toggleFavorite(noteId)
                        }
                    )
                }
            }

            composable(
                route = Screen.EditNote.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                val note = viewModel.getNoteById(noteId)

                note?.let {
                    EditNoteScreen(
                        note = it,
                        onSave = { title, content ->
                            viewModel.updateNote(noteId, title, content)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}