package com.example.ai_chat_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    //Sends the prompt to the server to receive a response
    @POST("gemini-chat")
    fun sendGeminiPrompt(@Body request: ChatRequest): Call<ChatResponse>
}
