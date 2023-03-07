package com.ch.bot.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ch.bot.databinding.MessageViewBinding
import com.ch.bot.model.BotMessage

class ChatBotMessageHolder(private val binding: MessageViewBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(message: BotMessage) {

        if (message.isSystem){
            binding.cardBotTitle.text = message.message
            binding.cardBot.visibility = View.GONE
            binding.cardBotTime.visibility = View.GONE
        }else{
            binding.cardBotMessage.text = message.message
            binding.cardBotTime.text = message.time
            binding.cardBot.visibility = View.VISIBLE
            binding.cardBotTime.visibility = View.VISIBLE
        }


    }
}