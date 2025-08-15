package com.example.ai_chat_app

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.json.Json

fun saveList(prefs: SharedPreferences, key: String, list: List<ChatMessage>) {
    val json = Json { ignoreUnknownKeys = true }

    val jsonString = json.encodeToString(list)
    prefs.edit { putString(key, jsonString) }
}
fun loadList(prefs: SharedPreferences, key: String): List<ChatMessage> {
    val json = Json { ignoreUnknownKeys = true }

    val jsonString = prefs.getString(key, null)
    return jsonString?.let { json.decodeFromString(it) } ?: emptyList()
}
