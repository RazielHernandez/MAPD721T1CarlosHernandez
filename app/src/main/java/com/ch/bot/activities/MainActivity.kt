package com.ch.bot.activities

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ch.bot.R
import com.ch.bot.service.NotificationBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var notificationBuilder: NotificationBuilder
    private lateinit var notificationMgr: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)

        title = "Other useless App"

        val fab: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(View.OnClickListener {
            //val edtUserName = findViewById(R.id.edtUserName) as EditText
            //val userName = edtUserName.text.toString()
            //saveToPreferences(userName)
            val intent = Intent(applicationContext, ChatBotActivity::class.java)
            startActivity(intent)

            Log.e("TAG", "Testing floating button")
        })

        notificationMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationBuilder(this, notificationMgr)

    }

}