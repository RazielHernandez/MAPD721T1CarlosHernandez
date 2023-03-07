package com.ch.bot.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ch.bot.R
import com.ch.bot.adapters.ChatBotMessageAdapter
import com.ch.bot.databinding.ActivityChatbotBinding
import com.ch.bot.model.BotMessage
import com.ch.bot.service.BotService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*


class ChatBotActivity: AppCompatActivity() {

    private val TAG = "com.ch.bot.activities:ChatBotActivity"

    private lateinit var binding: ActivityChatbotBinding
    private lateinit var adapter: ChatBotMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_chatbot)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerServiceStateChangeReceiver()

        title = "Friendly ChatBot"

        val fab: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab_send_message)
        fab.setOnClickListener(View.OnClickListener {

            if (binding.edtMessage.text.isEmpty()){
                binding.edtMessage.setError("Type your name first")

            }else{
                val userName = binding.edtMessage.text

                sendMessage("Hello $userName!!")
                sendMessage("How are you?")
                sendMessage("Good Bye $userName")

                binding.edtMessage.text.clear()
            }
        })

        binding.messageList.layoutManager = LinearLayoutManager(this)
        adapter = ChatBotMessageAdapter()
        binding.messageList.adapter = adapter

        startConnection("Connecting to chat bot")
        sendMessage("Hello! i want to help you, what's your name?")
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_chatbot, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_close -> {
                Log.e(TAG,"Close")
                endConnection("Closing chat bot connection")
                finish()
                return true
            }
            R.id.action_connect -> {
                Log.e(TAG, "Connect")
                startConnection("Connecting to chat bot")
                return true
            }
            R.id.action_message -> {
                Log.e(TAG,"Message")
                sendMessage("Testing chat bot message")
                return true
            }
            R.id.action_disconnect -> {
                endConnection("Ending chat bot session")
                Log.e(TAG,"Disconnect")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun endConnection(messageText: String) {
        val sdf = SimpleDateFormat("HH:mm")
        val currentTime: String = sdf.format(Date())

        val data = Bundle()
        data.putInt(BotService.CMD, BotService.CMD_STOP_CHAT)
        data.putString(BotService.KEY_MESSAGE_TEXT, messageText)
        data.putString(BotService.KEY_MESSAGE_TIME, currentTime)
        data.putString(BotService.KEY_MESSAGE_USER, "Friendly Bot")
        val intent = Intent(this, BotService::class.java)
        intent.putExtras(data)
        this.startService(intent)
    }

    private fun startConnection(messageText: String) {
        val sdf = SimpleDateFormat("HH:mm")
        val currentTime: String = sdf.format(Date())

        val data = Bundle()
        data.putInt(BotService.CMD, BotService.CMD_START_CHAT)
        data.putString(BotService.KEY_MESSAGE_TEXT, messageText)
        data.putString(BotService.KEY_MESSAGE_TIME, currentTime)
        data.putString(BotService.KEY_MESSAGE_USER, "Friendly Bot")
        val intent = Intent(this, BotService::class.java)
        intent.putExtras(data)
        this.startService(intent)
    }

    private fun sendMessage(messageText: String) {
        val sdf = SimpleDateFormat("HH:mm")
        val currentTime: String = sdf.format(Date())

        val data = Bundle()
        data.putInt(BotService.CMD, BotService.CMD_MESSAGE)
        data.putString(BotService.KEY_MESSAGE_TEXT, messageText)
        data.putString(BotService.KEY_MESSAGE_TIME, currentTime)
        val intent = Intent(this, BotService::class.java)
        intent.putExtras(data)
        this.startService(intent)
    }

    private fun sendSystemMessage(messageText: String) {
        val sdf = SimpleDateFormat("HH:mm")
        val currentTime: String = sdf.format(Date())

        val data = Bundle()
        data.putInt(BotService.CMD, BotService.CMD_SYSTEM)
        data.putString(BotService.KEY_MESSAGE_TEXT, messageText)
        data.putString(BotService.KEY_MESSAGE_TIME, currentTime)
        Log.e(TAG,"Time $currentTime")
        val intent = Intent(this, BotService::class.java)
        intent.putExtras(data)
        this.startService(intent)
    }

    fun displayMessage(message: BotMessage?) {
        if (message != null) {
            adapter?.add(message)
        }
        adapter!!.notifyDataSetChanged()
    }


    private fun registerServiceStateChangeReceiver() {
        Log.d(TAG, "registering service state change receiver...")
        val intentFilter = IntentFilter()
        intentFilter.addAction(BotService.BROADCAST_NEW_MESSAGE)
        intentFilter.addAction(BotService.BROADCAST_STARTED)
        intentFilter.addAction(BotService.BROADCAST_ENDED)

        val listenToBroadcastsFromOtherApps = false
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }

        ContextCompat.registerReceiver(this, broadCastReceiver, intentFilter, receiverFlags)
        //LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, intentFilter)
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        private val TAG = "BroadcastReceiver"
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val data = intent.extras
            Log.d(TAG, "received broadcast message from service: $action")

            if (BotService.BROADCAST_NEW_MESSAGE.equals(action)) {
                val userName = data!!.getString(BotService.KEY_MESSAGE_USER)
                val message = data.getString(BotService.KEY_MESSAGE_TEXT)
                val time = data.getString(BotService.KEY_MESSAGE_TIME)
                val newMessage: BotMessage = BotMessage(
                    userName.toString(), message.toString(), time.toString(), false)
                displayMessage(newMessage)
            } else if (BotService.BROADCAST_STARTED.equals(action)) {
                val userName = data!!.getString(BotService.KEY_MESSAGE_USER)
                val message = data.getString(BotService.KEY_MESSAGE_TEXT)
                val time = data.getString(BotService.KEY_MESSAGE_TIME)
                val newMessage: BotMessage = BotMessage(
                    userName.toString(), message.toString(), time.toString(), true)
                displayMessage(newMessage)
            } else if (BotService.BROADCAST_ENDED.equals(action)) {
                val userName = data!!.getString(BotService.KEY_MESSAGE_USER)
                val message = data.getString(BotService.KEY_MESSAGE_TEXT)
                val time = data.getString(BotService.KEY_MESSAGE_TIME)
                val newMessage: BotMessage = BotMessage(
                    userName.toString(), message.toString(), time.toString(), true)
                displayMessage(newMessage)
            } else if (BotService.BROADCAST_SYS_MESSAGE.equals(action)) {
                val userName = data!!.getString(BotService.KEY_MESSAGE_USER)
                val message = data.getString(BotService.KEY_MESSAGE_TEXT)
                val time = data.getString(BotService.KEY_MESSAGE_TIME)
                val newMessage: BotMessage = BotMessage(
                    userName.toString(), message.toString(), time.toString(), false)
                displayMessage(newMessage)
            } else {
                Log.v(TAG, "do nothing for action: $action")
            }
        }
    }
}