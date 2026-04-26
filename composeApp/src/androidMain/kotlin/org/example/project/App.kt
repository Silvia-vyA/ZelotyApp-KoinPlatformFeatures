package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.example.project.data.DatabaseDriverFactory
import org.example.project.data.NoteRepository
import org.example.project.data.SettingsManager
import org.example.project.di.appModule
import org.example.project.navigation.AppNavigation
import org.example.project.viewmodel.NotesViewModel
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val context = LocalContext.current

        val repository = remember {
            NoteRepository(
                driverFactory = DatabaseDriverFactory(context)
            )
        }

        val settingsManager = remember {
            SettingsManager()
        }

        val viewModel = remember {
            NotesViewModel(
                repository = repository,
                settingsManager = settingsManager
            )
        }

        val uiState by viewModel.uiState.collectAsState()

        MaterialTheme(
            colorScheme = if (uiState.isDarkMode) {
                darkColorSchemeCustom()
            } else {
                lightColorSchemeCustom()
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}