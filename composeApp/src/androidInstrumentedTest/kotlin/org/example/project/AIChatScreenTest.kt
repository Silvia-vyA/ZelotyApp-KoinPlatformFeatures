package org.example.project.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.ai.*
import org.example.project.viewmodel.Note
import org.junit.Rule
import org.junit.Test

class AIChatScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private fun mockViewModel(
        isLoading: Boolean = false
    ): AIViewModel {

        val vm = mockk<AIViewModel>(relaxed = true)

        every { vm.uiState } returns MutableStateFlow(
            AIUiState(
                messages = listOf(
                    AIMessage("Halo dari AI", false)
                ),
                isLoading = isLoading
            )
        )

        return vm
    }

    @Test
    fun screen_menampilkan_title() {

        rule.setContent {
            AIChatScreen(
                viewModel = mockViewModel(),
                notes = emptyList()
            )
        }

        rule.onNodeWithText("Zeloty AI 🤖")
            .assertIsDisplayed()
    }

    @Test
    fun tombol_action_tampil() {

        rule.setContent {
            AIChatScreen(
                viewModel = mockViewModel(),
                notes = emptyList()
            )
        }

        rule.onNodeWithText("Ringkas catatan terakhir")
            .assertIsDisplayed()

        rule.onNodeWithText("Translate catatan terakhir")
            .assertIsDisplayed()

        rule.onNodeWithText("Buat ide judul")
            .assertIsDisplayed()
    }

    @Test
    fun tombol_kirim_disable_saat_input_kosong() {

        rule.setContent {
            AIChatScreen(
                viewModel = mockViewModel(),
                notes = emptyList()
            )
        }

        rule.onNodeWithText("Kirim")
            .assertIsNotEnabled()
    }

    @Test
    fun tombol_kirim_enable_saat_input_diisi() {

        rule.setContent {
            AIChatScreen(
                viewModel = mockViewModel(),
                notes = emptyList()
            )
        }

        rule.onNodeWithText("Ketik pesan...")
            .performTextInput("Halo")

        rule.onNodeWithText("Kirim")
            .assertIsEnabled()
    }

    @Test
    fun klik_kirim_memanggil_sendMessage() {

        val vm = mockViewModel()

        rule.setContent {
            AIChatScreen(
                viewModel = vm,
                notes = emptyList()
            )
        }

        rule.onNodeWithText("Ketik pesan...")
            .performTextInput("Halo AI")

        rule.onNodeWithText("Kirim")
            .performClick()

        verify {
            vm.sendMessage("Halo AI")
        }
    }

    @Test
    fun klik_ringkas_memanggil_summarizeNote() {

        val vm = mockViewModel()

        val notes = listOf(
            Note(
                id = 1L,
                title = "Kotlin",
                content = "Belajar dasar kotlin"
            )
        )

        rule.setContent {
            AIChatScreen(
                viewModel = vm,
                notes = notes
            )
        }

        rule.onNodeWithText("Ringkas catatan terakhir")
            .performClick()

        verify {
            vm.summarizeNote(any())
        }
    }

    @Test
    fun loading_indicator_muncul_saat_loading() {

        rule.setContent {
            AIChatScreen(
                viewModel = mockViewModel(true),
                notes = emptyList()
            )
        }

        rule.onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }
}