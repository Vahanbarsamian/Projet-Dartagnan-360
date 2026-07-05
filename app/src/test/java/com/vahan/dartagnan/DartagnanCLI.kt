package com.vahan.dartagnan

import com.vahan.dartagnan.data.remote.OllamaApiService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DartagnanCLI {

    @Test
    fun ask() {
        println(">>> INITIALISATION DE D'ARTAGNAN <<<")
        
        val prompt = System.getProperty("prompt").takeIf { !it.isNullOrBlank() } ?: "Bonjour"
        val model = System.getProperty("model").takeIf { !it.isNullOrBlank() } ?: "llama3"
        
        val apiService = OllamaApiService()
        // En mode CLI local, on utilise localhost
        apiService.baseUrl = "localhost"

        println("\n⚔️  D'ARTAGNAN EXECUTION ⚔️")
        println("Question : $prompt")
        println("Modèle   : $model")
        println("--------------------------")

        runBlocking {
            val response = apiService.chat(prompt, model)
            println("\nRéponse de Dartagnan :\n$response")
        }
        println("--------------------------\n")
    }
}
