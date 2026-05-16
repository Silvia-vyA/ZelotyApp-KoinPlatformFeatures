package org.example.project.ai

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AIRepositoryTest {

    private val mockService = mockk<GeminiService>()
    private lateinit var repository: AIRepository

    @BeforeTest
    fun setup() {
        repository = AIRepository(mockService)
    }

    @Test
    fun `chat berhasil mengembalikan response`() = runTest {
        val message = "Halo"
        val response = "Halo juga"

        coEvery { mockService.generateContent(message) } returns Result.success(response)

        val result = repository.chat(message)

        assertTrue(result.isSuccess)
        assertEquals(response, result.getOrNull())

        coVerify { mockService.generateContent(message) }
    }

    @Test
    fun `chat gagal mengembalikan error`() = runTest {
        coEvery {
            mockService.generateContent(any())
        } returns Result.failure(Exception("Network Error"))

        val result = repository.chat("Test")

        assertTrue(result.isFailure)
        assertEquals("Network Error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `summarize membuat prompt yang benar`() = runTest {
        val text = "Belajar Kotlin"
        val prompt = """
            Ringkas catatan berikut dalam 2-3 kalimat:
            $text
        """.trimIndent()

        coEvery { mockService.generateContent(prompt) } returns Result.success("Ringkasan")

        repository.summarize(text)

        coVerify { mockService.generateContent(prompt) }
    }

    @Test
    fun `translate membuat prompt yang benar`() = runTest {
        val text = "Halo Dunia"
        val prompt = """
            Terjemahkan teks berikut ke Bahasa Inggris:
            $text
        """.trimIndent()

        coEvery { mockService.generateContent(prompt) } returns Result.success("Hello World")

        repository.translate(text)

        coVerify { mockService.generateContent(prompt) }
    }

    @Test
    fun `makeTitle membuat prompt yang benar`() = runTest {
        val text = "Catatan memasak"
        val prompt = """
            Buatkan 5 ide judul catatan yang menarik dari teks ini:
            $text
        """.trimIndent()

        coEvery { mockService.generateContent(prompt) } returns Result.success("Judul")

        repository.makeTitle(text)

        coVerify { mockService.generateContent(prompt) }
    }
}