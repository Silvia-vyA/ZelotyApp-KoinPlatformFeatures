package org.example.project.ai

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AIViewModelTest {

    private val mockRepo = mockk<AIRepository>()
    private lateinit var viewModel: AIViewModel

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = AIViewModel(mockRepo)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state menampilkan welcome message`() {
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(1, state.messages.size)
        assertTrue(state.messages.first().text.contains("Zeloty AI"))
    }

    @Test
    fun `sendMessage menambahkan pesan user dan response ai`() = runTest {
        val message = "Apa itu Kotlin?"

        coEvery {
            mockRepo.chat(message)
        } returns Result.success("Bahasa pemrograman")

        viewModel.uiState.test {

            awaitItem()

            viewModel.sendMessage(message)

            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val success = awaitItem()
            assertFalse(success.isLoading)
            assertEquals(3, success.messages.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sendMessage gagal menampilkan error`() = runTest {
        coEvery {
            mockRepo.chat(any())
        } returns Result.failure(Exception("Timeout"))

        viewModel.uiState.test {

            awaitItem()

            viewModel.sendMessage("Test")

            awaitItem()

            val error = awaitItem()

            assertFalse(error.isLoading)
            assertTrue(error.messages.last().text.contains("Timeout"))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sendMessage kosong tidak diproses`() = runTest {
        viewModel.sendMessage("")

        assertEquals(1, viewModel.uiState.value.messages.size)

        coVerify(exactly = 0) {
            mockRepo.chat(any())
        }
    }

    @Test
    fun `summarizeNote memanggil repository`() = runTest {
        val text = "Isi catatan"

        coEvery { mockRepo.chat(any()) } returns Result.success("Ringkasan")

        viewModel.summarizeNote(text)

        dispatcher.scheduler.advanceUntilIdle()

        coVerify {
            mockRepo.chat("Ringkas catatan ini:\n$text")
        }
    }
}