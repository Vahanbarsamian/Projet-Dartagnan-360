package com.vahan.dartagnan.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false
)

@Serializable
data class Message(
    val role: String,
    val content: String
)

@Serializable
data class ChatResponse(
    val model: String,
    val message: Message,
    val done: Boolean
)

@Serializable
data class TagsResponse(
    val models: List<OllamaModel>
)

@Serializable
data class OllamaModel(
    val name: String
)
