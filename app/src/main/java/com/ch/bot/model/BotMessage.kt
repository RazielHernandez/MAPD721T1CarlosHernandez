package com.ch.bot.model

data class BotMessage(
    val user: String,
    val message: String,
    val time: String,
    val isSystem: Boolean
)
