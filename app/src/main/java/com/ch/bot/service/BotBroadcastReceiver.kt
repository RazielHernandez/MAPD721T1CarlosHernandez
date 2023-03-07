package com.ch.bot.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BotBroadcastReceiver: BroadcastReceiver() {

    private val TAG = "BroadcastReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val data = intent.extras
        Log.d(TAG, "received broadcast message from service: $action")
        /*if (Constants.BROADCAST_SERVER_CONNECTED.equals(action)) {
            val chatMessage = ChatMessage("Status: ", "Connected", true)
            displayMessage(chatMessage)
        } else if (Constants.BROADCAST_SERVER_NOT_CONNECTED.equals(action)) {
            val chatMessage = ChatMessage("Status: ", "Disconnected", true)
            displayMessage(chatMessage)
        } else if (Constants.BROADCAST_USER_JOINED.equals(action)) {
            val userName = data!!.getString(Constants.CHAT_USER_NAME)
            val userCount = data.getInt(Constants.CHAT_USER_COUNT, 0)
            val chatMessage = ChatMessage(
                userName,
                " joined. Users: $userCount", true
            )
            displayMessage(chatMessage)
        } else if (Constants.BROADCAST_USER_LEFT.equals(action)) {
            val userName = data!!.getString(Constants.CHAT_USER_NAME)
            val userCount = data.getInt(Constants.CHAT_USER_COUNT, 0)
            val chatMessage = ChatMessage(userName, " left. Users: $userCount", true)
            displayMessage(chatMessage)
        } else if (Constants.BROADCAST_NEW_MESSAGE.equals(action)) {
            val userName = data!!.getString(Constants.CHAT_USER_NAME)
            val message = data.getString(Constants.CHAT_MESSAGE)
            val chatMessage = ChatMessage(userName, message)
            displayMessage(chatMessage)
        } else if (Constants.BROADCAST_USER_TYPING.equals(action)) {
            // TODO
        } else {
            Log.v(TAG, "do nothing for action: $action")
        }*/
    }
}