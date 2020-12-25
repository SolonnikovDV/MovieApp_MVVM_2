package com.example.movieapp_mvvm.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.util.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.example.movieapp_mvvm.util.Constants.Companion.NOTIFICATION_ICON
import com.example.movieapp_mvvm.util.Constants.Companion.NOTIFICATION_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//d1u2QpsUQCOT9zrBTjX4bi:APA91bHmlj_ro9I5Rdt9_3MFTtX9pKnDJcdwHCCQ5DpNJ0gbvh6O74pHKX6OXSFAOkVZInld2MkxSIW2ZxFoLY1-m9efEF9xkVW6i6WLq1OqYO2zc3vORwIOO7fr81uQ9vmHIlDqx7yC

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"

//    val LINK = "https://www.themoviedb.org/tv/97180-selena-the-series"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("message", "Message Received ...");

        if (remoteMessage.data.size > 0) {
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            val link = remoteMessage.data["link"]
            showNotification(applicationContext, title, body, link)
        } else {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            val link = remoteMessage.notification!!.link.toString()
            showNotification(applicationContext, title, body, link)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token", "New Token $token")
    }

    fun showNotification(context: Context, title: String?, message: String?, link: String?) {

        val notificationIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(link)
            }

        val pendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification: Notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(NOTIFICATION_ICON)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}