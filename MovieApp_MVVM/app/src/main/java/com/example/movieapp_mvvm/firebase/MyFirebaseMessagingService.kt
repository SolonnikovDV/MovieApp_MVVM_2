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
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//d1u2QpsUQCOT9zrBTjX4bi:APA91bHmlj_ro9I5Rdt9_3MFTtX9pKnDJcdwHCCQ5DpNJ0gbvh6O74pHKX6OXSFAOkVZInld2MkxSIW2ZxFoLY1-m9efEF9xkVW6i6WLq1OqYO2zc3vORwIOO7fr81uQ9vmHIlDqx7yC

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"
    var NOTIFICATION_CHANNEL_ID = "MovieApp_MVVM/app"
    val NOTIFICATION_ID = 100
    val NOTIFICATION_ICON = R.drawable.ic_logo
    val LINK = "https://www.themoviedb.org/tv/97180-selena-the-series"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("message", "Message Received ...");

        if (remoteMessage.data.size > 0) {
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            showNotification(applicationContext, title, body, LINK)
        } else {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            showNotification(applicationContext, title, body, LINK)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token", "New Token $token")
    }

    fun showNotification(context: Context, title: String?, message: String?, link: String?) {

        val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                data = Uri.parse("custom://" + System.currentTimeMillis())
                action = "actionstring" + System.currentTimeMillis()
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP

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

        } else {
            notification = NotificationCompat.Builder(context)
                .setSmallIcon(NOTIFICATION_ICON)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}