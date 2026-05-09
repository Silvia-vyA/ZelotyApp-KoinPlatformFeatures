package org.example.project.ai

class AIRepository(
    private val geminiService: GeminiService
) {
    suspend fun chat(message: String): Result<String> {
        return geminiService.generateContent(message)
    }

    suspend fun summarize(text: String): Result<String> {
        val prompt = """
            Ringkas catatan berikut dalam 2-3 kalimat:
            $text
        """.trimIndent()

        return geminiService.generateContent(prompt)
    }

    suspend fun translate(text: String): Result<String> {
        val prompt = """
            Terjemahkan teks berikut ke Bahasa Inggris:
            $text
        """.trimIndent()

        return geminiService.generateContent(prompt)
    }

    suspend fun makeTitle(text: String): Result<String> {
        val prompt = """
            Buatkan 5 ide judul catatan yang menarik dari teks ini:
            $text
        """.trimIndent()

        return geminiService.generateContent(prompt)
    }
}