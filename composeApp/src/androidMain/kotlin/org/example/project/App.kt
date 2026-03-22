package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.ui.ProfileScreen
import org.example.project.viewmodel.ProfileViewModel

@Composable
fun App() {
    val viewModel: ProfileViewModel = viewModel()
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
            ProfileScreen(viewModel = viewModel)
        }
    }
}