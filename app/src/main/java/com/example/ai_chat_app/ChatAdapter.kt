package com.example.ai_chat_app

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //what will be sent to the view
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageText.text = messages[position].message

        // Get the LinearLayout that contains the chat item
        val layout = holder.itemView.findViewById<LinearLayout>(R.id.chatItemContainer)

        // Set the background and alignment based on whether the message is from the user or AI
        if (messages[position].isUser) {
            layout.gravity = Gravity.END
            holder.messageText.setBackgroundResource(R.drawable.chat_bubble_user)
            holder.messageText.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        } else {
            layout.gravity = Gravity.START
            holder.messageText.setBackgroundResource(R.drawable.chat_bubble_ai)
            holder.messageText.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        }
    }

    //length of chat history
    override fun getItemCount() = messages.size
}
