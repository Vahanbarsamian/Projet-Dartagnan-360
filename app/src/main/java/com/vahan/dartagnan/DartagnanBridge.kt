package com.vahan.dartagnan

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import java.io.File

fun main() {
    val userHome = System.getProperty("user.home") ?: ""
    val projectRoot = System.getProperty("user.dir") ?: ""
    
    // CONFIGURATION DU CLIENT AVEC PATIENCE INFINIE (Timeout)
    val client = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = 600000 // 10 minutes d'attente
            connectTimeoutMillis = 600000
            socketTimeoutMillis = 600000
        }
    }

    var desktopPath = File(userHome, "Desktop")
    if (File(userHome, "OneDrive/Bureau").exists()) desktopPath = File(userHome, "OneDrive/Bureau")
    else if (File(userHome, "OneDrive/Desktop").exists()) desktopPath = File(userHome, "OneDrive/Desktop")
    
    val desktopDir = File(desktopPath, "Dartagnan_Rendus").absolutePath
    val archivesDir = File(desktopPath, "Dartagnan_Archives")
    
    if (!archivesDir.exists()) archivesDir.mkdirs()
    if (!File(desktopDir).exists()) File(desktopDir).mkdirs()

    println("🔱 LE PONT ROYAL V5.2 EST ACTIF (Mode Patience Infinie)")

    embeddedServer(Netty, port = 8081) {
        install(CORS) {
            anyHost()
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Get)
            allowHeader(HttpHeaders.ContentType)
        }
        install(ContentNegotiation) {
            json()
        }
        
        routing {
            post("/save") { saveFile(call, desktopDir) }
            post("/apply") { saveFile(call, projectRoot) }
            
            post("/ollama/chat") {
                val body = call.receiveText()
                try {
                    val response: HttpResponse = client.post("http://127.0.0.1:11434/api/chat") {
                        setBody(body)
                    }
                    call.respondText(response.bodyAsText(), ContentType.Application.Json)
                } catch (e: Exception) {
                    println("❌ Erreur Ollama : ${e.message}")
                    call.respondText("{\"error\":\"Ollama a mis trop de temps a repondre\"}", ContentType.Application.Json, HttpStatusCode.GatewayTimeout)
                }
            }

            get("/ollama/tags") {
                try {
                    val response: HttpResponse = client.get("http://127.0.0.1:11434/api/tags")
                    call.respondText(response.bodyAsText(), ContentType.Application.Json)
                } catch (e: Exception) {
                    call.respondText("{\"models\":[]}", ContentType.Application.Json)
                }
            }

            post("/archive") {
                val params = call.receiveParameters()
                val content = params["content"] ?: ""
                val index = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
                File(archivesDir, "archive_$index.json").writeText(content)
                call.respondText("📚 Archive sauvegardée !")
            }

            get("/archives") {
                val files = archivesDir.listFiles { _, name -> name.endsWith(".json") }
                    ?.map { it.name.removePrefix("archive_").removeSuffix(".json") }
                    ?.sortedDescending() ?: emptyList()
                call.respond(files)
            }

            post("/forge") {
                val params = call.receiveParameters()
                val command = params["command"] ?: ""
                try {
                    val isService = command.contains("serve") || command.contains("start")
                    if (isService) {
                        Runtime.getRuntime().exec("cmd /c start /min $command")
                        call.respondText("⚡ Service lancé.")
                    } else {
                        val process = Runtime.getRuntime().exec(command)
                        val result = process.waitFor()
                        call.respondText(if (result == 0) "✅ Forge réussie." else "❌ Erreur $result")
                    }
                } catch (e: Exception) { call.respondText("❌ Erreur : ${e.message}") }
            }
        }
    }.start(wait = true)
}

suspend fun saveFile(call: ApplicationCall, baseDir: String) {
    val params = call.receiveParameters()
    val path = params["path"] ?: ""
    val content = params["content"] ?: ""
    val name = params["name"] ?: "rendu_${System.currentTimeMillis()}.txt"
    val targetFile = if (path.isNotEmpty()) File(baseDir, path) else File(baseDir, name)
    targetFile.parentFile?.mkdirs()
    targetFile.writeText(content)
    call.respondText("🔱 Succès : ${targetFile.name}")
}
