package com.vahan.dartagnan.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class OllamaApiService {
    var baseUrl: String = "10.0.2.2"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val SYSTEM_PROMPT = """
        Tu es Dartagnan, un ingénieur logiciel senior et architecte expert Android/Kotlin. 
        Ton expertise est le "Clean Code", la performance algorithmique et les architectures modernes.
        
        Tes règles :
        1. Réponds avec une précision technique absolue.
        2. Si on te présente du code, analyse les fuites de mémoire, la complexité cyclomatique et l'efficacité.
        3. Propose toujours la solution la plus optimisée et explique POURQUOI elle l'est.
        4. Utilise un ton professionnel, direct, mais garde une pointe de noblesse (mousquetaire).
        5. Priorise toujours la sécurité et la maintenabilité du code.
    """.trimIndent()

    suspend fun chat(prompt: String, model: String): String {
        return try {
            val response: ChatResponse = client.post("http://$baseUrl:11434/api/chat") {
                contentType(ContentType.Application.Json)
                setBody(ChatRequest(
                    model = model,
                    messages = listOf(
                        Message(role = "system", content = SYSTEM_PROMPT),
                        Message(role = "user", content = prompt)
                    ),
                    stream = false
                ))
            }.body()
            response.message.content
        } catch (e: Exception) {
            "Erreur: ${e.localizedMessage}. Assurez-vous qu'Ollama est lancé sur http://$baseUrl:11434"
        }
    }

    suspend fun getModels(): List<String> {
        return try {
            val response: TagsResponse = client.get("http://$baseUrl:11434/api/tags").body()
            response.models.map { it.name }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
