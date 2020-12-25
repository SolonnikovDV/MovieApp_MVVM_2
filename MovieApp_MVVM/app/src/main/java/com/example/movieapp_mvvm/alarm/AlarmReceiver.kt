package com.example.movieapp_mvvm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.ui.fragments.MoviesFragment
import com.example.movieapp_mvvm.util.Constants.Companion.ACTION_SET_EXACT
import com.example.movieapp_mvvm.util.Constants.Companion.CHANNEL_ID
import com.example.movieapp_mvvm.util.Constants.Companion.CHANNEL_NAME
import com.example.movieapp_mvvm.util.Constants.Companion.EXTRA_EXACT_ALARM_TIME
import com.example.movieapp_mvvm.util.Constants.Companion.NOTIFICATION_ID

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(EXTRA_EXACT_ALARM_TIME, 0L)
        if (intent.action == ACTION_SET_EXACT) {
            createNotificationChannel(context)
            buildNotification(context, "Reminding", convertDate(timeInMillis))
        }
    }


    private fun buildNotification(context: Context, title: String, message: String){

//building notification with io.karn:notify library
//        Notify
//            .with(context)
//            .content {
//                this.title = title
//                text = "You've set remind time at: - $message"
//            }
//            .show()

        //TODO
        // put here the clicked item
        val notificationIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.google.com")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("You've set remind time at: - $message")
            .setSmallIcon(R.drawable.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply {
                    lightColor = Color.GREEN
                    enableLights(true)
                }

            val notificationManager = getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}

