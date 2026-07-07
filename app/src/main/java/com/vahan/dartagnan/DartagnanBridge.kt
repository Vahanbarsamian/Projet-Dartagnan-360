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
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import java.io.File

fun main() {
    val userHome = System.getProperty("user.home") ?: ""
    val projectRoot = System.getProperty("user.dir") ?: ""
    
    val client = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = 600000 
            connectTimeoutMillis = 600000
            socketTimeoutMillis = 600000
        }
    }

    var desktopFile = File(userHome, "Desktop")
    if (File(userHome, "OneDrive/Bureau").exists()) desktopFile = File(userHome, "OneDrive/Bureau")
    else if (File(userHome, "OneDrive/Desktop").exists()) desktopFile = File(userHome, "OneDrive/Desktop")

    val rendusDir = File(desktopFile, "Dartagnan_Rendus")
    val archivesDir = File(desktopFile, "Dartagnan_Archives")
    
    if (!archivesDir.exists()) archivesDir.mkdirs()
    if (!rendusDir.exists()) rendusDir.mkdirs()

    println("🔱 LE PONT ROYAL V6.0 EST ACTIF")

    embeddedServer(Netty, port = 8081) {
        install(CORS) {
            anyHost()
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Get)
            allowHeader(HttpHeaders.ContentType)
        }
        install(ContentNegotiation) { json() }
        
        routing {
            post("/save") { saveFileAsync(call, rendusDir.absolutePath) }
            post("/apply") { saveFileAsync(call, projectRoot) }
            
            post("/ollama/chat") {
                val body = call.receiveText()
                withContext(Dispatchers.IO) {
                    try {
                        val response: HttpResponse = client.post("http://127.0.0.1:11434/api/chat") { setBody(body) }
                        call.respondText(response.bodyAsText(), ContentType.Application.Json)
                    } catch (e: Exception) {
                        call.respondText("{\"error\":\"Ollama indisponible\"}", ContentType.Application.Json, HttpStatusCode.ServiceUnavailable)
                    }
                }
            }

            // NOUVEAU : RELAIS DE FLUX POUR LE TÉLÉCHARGEMENT (PULL)
            post("/ollama/pull") {
                val body = call.receiveText()
                try {
                    client.preparePost("http://127.0.0.1:11434/api/pull") {
                        setBody(body)
                    }.execute { response ->
                        call.respondBytesWriter(ContentType.Application.Json) {
                            val channel = response.bodyAsChannel()
                            while (!channel.isClosedForRead) {
                                val byteBuffer = ByteArray(1024)
                                val read = channel.readAvailable(byteBuffer)
                                if (read > 0) {
                                    writeFully(byteBuffer, 0, read)
                                    flush()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    call.respondText("{\"error\":\"Echec du relais de flux\"}", ContentType.Application.Json)
                }
            }

            get("/ollama/tags") {
                withContext(Dispatchers.IO) {
                    try {
                        val response: HttpResponse = client.get("http://127.0.0.1:11434/api/tags")
                        call.respondText(response.bodyAsText(), ContentType.Application.Json)
                    } catch (e: Exception) {
                        call.respondText("{\"models\":[]}", ContentType.Application.Json)
                    }
                }
            }

            post("/archive") {
                val params = call.receiveParameters()
                val content = params["content"] ?: ""
                val index = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
                withContext(Dispatchers.IO) {
                    File(archivesDir, "archive_$index.json").writeText(content)
                }
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
                launch(Dispatchers.IO) {
                    try {
                        val isService = command.contains("serve") || command.contains("start")
                        if (isService) {
                            Runtime.getRuntime().exec("cmd /c start /min $command")
                        } else {
                            val process = Runtime.getRuntime().exec(command)
                            process.waitFor()
                        }
                    } catch (e: Exception) { 
                        println("❌ Erreur Forge: ${e.message}") 
                    }
                }
                call.respondText("⚡ Ordre de Forge envoyé.")
            }
        }
    }.start(wait = true)
}

suspend fun saveFileAsync(call: ApplicationCall, baseDir: String) {
    val params = call.receiveParameters()
    val path = params["path"] ?: ""
    val content = params["content"] ?: ""
    val name = params["name"] ?: "rendu_${System.currentTimeMillis()}.txt"
    val targetFile = if (path.isNotEmpty()) File(baseDir, path) else File(baseDir, name)
    
    withContext(Dispatchers.IO) {
        targetFile.parentFile?.mkdirs()
        targetFile.writeText(content)
    }
    call.respondText("🔱 Succès : ${targetFile.name}")
}
