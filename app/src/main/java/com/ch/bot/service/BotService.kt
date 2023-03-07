package com.ch.bot.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log

class BotService: Service() {

    private val TAG = "com.ch.bot.service:BotService"

    companion object {
        var BASE = "com.zv.geochat."
        var BROADCAST_STARTED = BASE + "STARTING_BROADCAST"
        var BROADCAST_NEW_MESSAGE = BASE + "NEW_MESSAGE"
        var BROADCAST_ENDED = BASE + "ENDING_BROADCAST"
        var BROADCAST_SYS_MESSAGE = BASE + "SYS_MESSAGE"

        val CMD = "bot_cmd"
        val CMD_START_CHAT = 10
        val CMD_MESSAGE = 20
        val CMD_STOP_CHAT = 30
        val CMD_SYSTEM = 40

        val KEY_MESSAGE_USER = "BOT_USER"
        val KEY_MESSAGE_TEXT = "BOT_TEXT"
        val KEY_MESSAGE_TIME = "BOT_TIME"
        val KEY_MESSAGE_ERROR = "BOT_ERROR"

    }

    private var notificationMgr: NotificationManager? = null
    private var wakeLock: WakeLock? = null
    private var notificationBuilder: NotificationBuilder? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.v(TAG, "onCreate()")
        super.onCreate()
        notificationMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationBuilder(this, notificationMgr!!)
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "onStartCommand()")
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            val data = intent.extras
            if (data != null) {
                handleData(data)
            }
            if (!wakeLock!!.isHeld) {
                Log.v(TAG, "acquiring wake lock")
                wakeLock!!.acquire()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy()")
        notificationMgr!!.cancelAll()
        Log.v(TAG, "releasing wake lock")
        wakeLock!!.release()
        super.onDestroy()
    }

    private fun handleData(data: Bundle) {
        val command = data.getInt(CMD)
        Log.d(TAG, "-(<- received command data to service: command=$command")
        if (command == CMD_START_CHAT) {
            val message = data.getString(BotService.KEY_MESSAGE_TEXT)
            val time = data.getString(BotService.KEY_MESSAGE_TIME)
            val user = data.getString(BotService.KEY_MESSAGE_USER)
            notificationBuilder?.sendNotification("ChatBot Started: 63", "ChatBot Started: 63")
            sendBroadcastConnected(user.toString(), time.toString(), message.toString())
        } else if (command == CMD_MESSAGE) {
            val message = data.getString(BotService.KEY_MESSAGE_TEXT)
            val time = data.getString(BotService.KEY_MESSAGE_TIME)
            val user = data.getString(BotService.KEY_MESSAGE_USER)
            notificationBuilder?.sendNotification("ChatBot New Message", "ChatBot New Message")
            sendBroadcastNewMessage(user.toString(), time.toString(), message.toString())
        } else if (command == CMD_STOP_CHAT) {
            val message = data.getString(BotService.KEY_MESSAGE_TEXT)
            val time = data.getString(BotService.KEY_MESSAGE_TIME)
            val user = data.getString(BotService.KEY_MESSAGE_USER)
            notificationBuilder?.sendNotification("ChatBot Stopped: 63", "ChatBot Stopped: 63")
            sendBroadcastDisconnected(user.toString(), time.toString(), message.toString())
        } else if (command == CMD_SYSTEM) {
            val message = data.getString(BotService.KEY_MESSAGE_TEXT)
            val time = data.getString(BotService.KEY_MESSAGE_TIME)
            val user = data.getString(BotService.KEY_MESSAGE_USER)
            notificationBuilder?.sendNotification("ChatBot System Message", "ChatBot System Message")
            sendBroadcastSystemMessage(user.toString(), time.toString(), message.toString())
        } else {
            Log.w(TAG, "Ignoring Unknown Command! id=$command")
        }
    }

    private fun sendBroadcastConnected(user: String, time: String, message: String) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SERVER_CONNECTED")
        val intent = Intent()
        intent.action = BROADCAST_STARTED
        val data = Bundle()
        data.putString(BotService.KEY_MESSAGE_USER, "Friendly bot")
        data.putString(BotService.KEY_MESSAGE_TEXT, message)
        data.putString(BotService.KEY_MESSAGE_TIME, time)
        intent.putExtras(data)
        sendBroadcast(intent)
    }

    private fun sendBroadcastNewMessage(user: String, time: String, message: String) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SERVER_NEW_MESSAGE")
        val intent = Intent()
        intent.action = BROADCAST_NEW_MESSAGE
        val data = Bundle()
        data.putString(BotService.KEY_MESSAGE_USER, "Friendly bot")
        data.putString(BotService.KEY_MESSAGE_TEXT, message)
        data.putString(BotService.KEY_MESSAGE_TIME, time)
        intent.putExtras(data)
        sendBroadcast(intent)
    }

    private fun sendBroadcastDisconnected(user: String, time: String, message: String) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SERVER_DISCONNECTED")
        val intent = Intent()
        intent.action = BROADCAST_ENDED
        val data = Bundle()
        data.putString(BotService.KEY_MESSAGE_USER, "Friendly bot")
        data.putString(BotService.KEY_MESSAGE_TEXT, message)
        data.putString(BotService.KEY_MESSAGE_TIME, time)
        intent.putExtras(data)
        sendBroadcast(intent)
    }

    private fun sendBroadcastSystemMessage(user: String, time: String, message: String) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SERVER_DISCONNECTED")
        val intent = Intent()
        intent.action = BROADCAST_SYS_MESSAGE
        val data = Bundle()
        data.putString(BotService.KEY_MESSAGE_USER, "Friendly bot")
        data.putString(BotService.KEY_MESSAGE_TEXT, message)
        data.putString(BotService.KEY_MESSAGE_TIME, time)
        intent.putExtras(data)
        sendBroadcast(intent)
    }


}