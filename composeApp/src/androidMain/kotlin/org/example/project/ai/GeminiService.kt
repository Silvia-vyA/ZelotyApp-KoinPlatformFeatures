package org.example.project.ai

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.json.JSONArray
import org.json.JSONObject

class GeminiService(
    private val client: HttpClient,
    private val apiKey: String
) {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val model = "gemini-2.0-flash"

    suspend fun generateContent(userMessage: String): Result<String> {
        return runCatching {
            if (apiKey.isBlank()) {
                error("API key Gemini belum terbaca. Cek local.properties.")
            }

            val prompt = """
                Kamu adalah Zeloty AI, asisten catatan pribadi.
                Jawab pertanyaan user dengan Bahasa Indonesia yang jelas, singkat, dan ramah.
                Jangan jawab dengan template error.
                Jika user meminta ide, berikan beberapa ide yang kreatif.
                
                Pertanyaan user:
                $userMessage
            """.trimIndent()

            val requestBody = JSONObject()
                .put(
                    "contents",
                    JSONArray().put(
                        JSONObject()
                            .put("role", "user")
                            .put(
                                "parts",
                                JSONArray().put(
                                    JSONObject().put("text", prompt)
                                )
                            )
                    )
                )
                .put(
                    "generationConfig",
                    JSONObject()
                        .put("temperature", 0.8)
                        .put("maxOutputTokens", 700)
                        .put("topP", 0.95)
                )
                .toString()

            val response = client.post("$baseUrl/models/$model:generateContent") {
                contentType(ContentType.Application.Json)
                parameter("key", apiKey)
                setBody(requestBody)
            }

            val responseBody: String = response.body()
            parseGeminiResponse(responseBody)
        }
    }

    private fun parseGeminiResponse(responseBody: String): String {
        val json = JSONObject(responseBody)

        if (json.has("error")) {
            val error = json.getJSONObject("error")
            val message = error.optString("message", "Terjadi error dari Gemini.")
            error("Gemini error: $message")
        }

        val candidates = json.optJSONArray("candidates")
        if (candidates == null || candidates.length() == 0) {
            error("Gemini belum mengirim jawaban. Coba ubah pertanyaan atau cek API key.")
        }

        val firstCandidate = candidates.getJSONObject(0)
        val content = firstCandidate.optJSONObject("content")
            ?: error("Response Gemini tidak punya content.")

        val parts = content.optJSONArray("parts")
            ?: error("Response Gemini tidak punya parts.")

        if (parts.length() == 0) {
            error("Jawaban Gemini kosong.")
        }

        return parts
            .getJSONObject(0)
            .optString("text", "")
            .trim()
            .ifBlank { "Gemini menjawab kosong. Coba tanyakan ulang dengan kalimat lebih jelas." }
    }
}