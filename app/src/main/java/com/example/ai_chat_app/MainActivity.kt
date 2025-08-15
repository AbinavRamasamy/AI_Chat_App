package com.example.ai_chat_app

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.toMutableList

class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val chatMessages = loadList(prefs, "chat").toMutableList()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top,
                        systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        chatAdapter = ChatAdapter(chatMessages)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Start at bottom of chat at app launch
        recyclerView.scrollToPosition(chatMessages.size - 1)

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // replace with your IP
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create API service
        val chatApi = retrofit.create(ChatApi::class.java)

        // Initialize UI components
        val trash = findViewById<FloatingActionButton>(R.id.trashChat)
        val sendButton = findViewById<FloatingActionButton>(R.id.sendButton)
        val promptEditText = findViewById<EditText>(R.id.promptEditText)

        trash.setOnClickListener {
            chatMessages.clear()
            saveList(prefs, "chat", chatMessages)
            recyclerView.scrollToPosition(chatMessages.size - 1)
            Toast.makeText(this, "Chat Cleared",
                            Toast.LENGTH_SHORT).show()
        }

        // Handle Enter key for sending messages
        sendButton.setOnClickListener {
            // Get user input
            val promptText = promptEditText.text.toString().trim()

            // Validate input
            if (promptText.isBlank()) {
                Toast.makeText(this, "Please enter a prompt",
                                Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Add user message to chat
            chatMessages.add(ChatMessage(promptText, isUser = true))

            chatAdapter.notifyItemInserted(chatMessages.size - 1)
            recyclerView.scrollToPosition(chatMessages.size - 1)

            // Clear input
            promptEditText.setText("")

            // Send request
            val request = ChatRequest(text = promptText)
            chatApi.sendGeminiPrompt(request).enqueue(object : Callback<ChatResponse> {
                override fun onResponse(
                    call: Call<ChatResponse>,
                    response: Response<ChatResponse>
                ) {
                    if (response.isSuccessful) {
                        val aiReply = response.body()?.reply ?: "No reply"
                        chatMessages.add(ChatMessage(aiReply, isUser = false))
                        saveList(prefs, "chat", chatMessages)
                        chatAdapter.notifyItemInserted(chatMessages.size - 1)
                        recyclerView.scrollToPosition(chatMessages.size - 1)
                    } else {
                        Log.e("AI_CHAT", "Gemini Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    Log.e("AI_CHAT", "Gemini Network Error", t)
                    chatMessages.add(ChatMessage("Network error: " +
                                        "${t.localizedMessage}", isUser = false))
                    chatAdapter.notifyItemInserted(chatMessages.size - 1)
                    recyclerView.scrollToPosition(chatMessages.size - 1)
                }
            })
        }
    }
}
