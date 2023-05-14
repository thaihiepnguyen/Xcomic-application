package com.example.x_comic.views.main

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.x_comic.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.x_comic"

class MyFirebaseMessagingService:FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
// Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            if (/* Check if data needs to be processed by long running job */ true) {
// For long-running tasks (10s or more) use WorkManager.

            } else {
// Handle message within 10 seconds

            }
        }
// Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
//Generate your own notification as a result of a received FCM
        sendNotification(remoteMessage.notification?.body ?: "empty_body")
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String):RemoteViews{
        val remoteView = RemoteViews("com.example.x_comic", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.books)

        return remoteView
    }
    private fun sendNotification(messageBody: String) {
//MainActivity is the activity will be show when you tap this notification
// you can change it with your own activity
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */,

            intent, PendingIntent.FLAG_IMMUTABLE
        )

        val default_notification_channel_id = channelId
        val fcm_message = "fcm_message title"
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(
            this,

            default_notification_channel_id
        )

            .setContentTitle(fcm_message).setContentText(messageBody)
            .setAutoCancel(true).setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setSmallIcon(com.facebook.share.R.drawable.abc_btn_default_mtrl_shape)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
// Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                default_notification_channel_id,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(
            0/*ID of notification*/,

            notificationBuilder.build()
        )
    }
}