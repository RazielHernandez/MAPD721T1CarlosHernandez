package com.ch.bot.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.ch.bot.R
import com.ch.bot.activities.MainActivity


class NotificationBuilder(context: Context, notificationManager: NotificationManager ) {

    private val TAG = "com.ch.bot.service:NotificationBuilder"
    val CHANNEL_ID = "com.ch.bot.service.channel"


    private val context: Context? = context
    private val notificationMgr: NotificationManager? = notificationManager
    //private val messageNotifierConfig: MessageNotifierConfig? = null

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("TAG","adding channel")
            val name = "getString(R.string.channel_name)"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationMgr?.createNotificationChannel(channel)
        }
    }

    fun sendNotification(title: String?, contentText: String?, id: Int) {

        val notificationIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            context, 0, notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        if (context == null)     return
        val remoteViews = RemoteViews(context!!.packageName, R.layout.notification_simple_view)
        remoteViews.setImageViewResource(R.id.image_left, R.drawable.ic_email)
        remoteViews.setImageViewResource(R.id.image_right, R.drawable.ic_email)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.text, contentText)

        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_email)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setContent(remoteViews)
            .build()

        // Add as notification
        notificationMgr?.notify(id, notification)
    }


}