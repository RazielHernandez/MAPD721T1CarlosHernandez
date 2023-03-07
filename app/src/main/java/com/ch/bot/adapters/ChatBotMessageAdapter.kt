package com.ch.bot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ch.bot.databinding.MessageViewBinding
import com.ch.bot.model.BotMessage

class ChatBotMessageAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatMessages: MutableList<BotMessage> = mutableListOf<BotMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MessageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatBotMessageHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChatBotMessageHolder).bind(chatMessages[position])
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    fun add(message: BotMessage) {
        chatMessages!!.add(message)
    }

    fun add(messages: List<BotMessage>?) {
        chatMessages!!.addAll(messages!!)
    }
}