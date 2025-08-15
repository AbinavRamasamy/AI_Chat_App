package com.example.ai_chat_app

// Parts of a message
data class ChatMessage(
    val message: String,
    val isUser: Boolean
)

// Request from user to AI
data class ChatRequest(
    val text: String
)

// Reply text from AI
data class ChatResponse(
    val reply: String
)
