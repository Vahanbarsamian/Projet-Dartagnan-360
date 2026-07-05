package com.vahan.dartagnan

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
    val projectRoot = "C:/Users/vahan/AndroidStudioProjects/Dartagnan"
    val desktopDir = "C:/Users/vahan/OneDrive/Bureau/Dartagnan_Rendus"
    val archivesDir = File("C:/Users/vahan/OneDrive/Bureau/Dartagnan_Archives")

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
            
            post("/archive") {
                val params = call.receiveParameters()
                val content = params["content"] ?: ""
                val index = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
                if (!archivesDir.exists()) archivesDir.mkdirs()
                File(archivesDir, "archive_$index.json").writeText(content)
                call.respondText("📚 Archive #$index sauvegardée !")
            }

            get("/archives") {
                if (!archivesDir.exists()) {
                    call.respond(emptyList<String>())
                } else {
                    val files = archivesDir.listFiles { _, name -> name.endsWith(".json") }
                        ?.map { it.name.removePrefix("archive_").removeSuffix(".json") }
                        ?.sortedDescending() ?: emptyList()
                    call.respond(files)
                }
            }
            
            get("/archive/{id}") {
                val id = call.parameters["id"]
                val file = File(archivesDir, "archive_$id.json")
                if (file.exists()) {
                    call.respondText(file.readText())
                } else {
                    call.respondText("Archive introuvable", status = HttpStatusCode.NotFound)
                }
            }

            post("/forge") {
                val params = call.receiveParameters()
                val command = params["command"] ?: ""
                try {
                    // On detecte si c'est un lancement de service (comme ollama) pour ne pas bloquer
                    val isService = command.contains("serve") || command.contains("start")
                    val process = if (isService) {
                        Runtime.getRuntime().exec("cmd /c start /min $command")
                    } else {
                        Runtime.getRuntime().exec(command)
                    }
                    
                    if (isService) {
                        call.respondText("⚡ Service lancé en arrière-plan.")
                    } else {
                        val result = process.waitFor()
                        if (result == 0) call.respondText("✅ Forge réussie.")
                        else call.respondText("❌ Erreur code $result", status = HttpStatusCode.InternalServerError)
                    }
                } catch (e: Exception) {
                    call.respondText("❌ Erreur : ${e.message}", status = HttpStatusCode.InternalServerError)
                }
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
    call.respondText("🔱 Action réussie : ${targetFile.name}")
}
