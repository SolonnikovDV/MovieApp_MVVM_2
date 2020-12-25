package com.example.movieapp_mvvm.alarm

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateFormat
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.util.Constants
import com.example.movieapp_mvvm.util.Constants.Companion.ACTION_SET_EXACT
import com.example.movieapp_mvvm.util.Constants.Companion.EXTRA_EXACT_ALARM_TIME
import io.karn.notify.Notify
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(EXTRA_EXACT_ALARM_TIME, 0L)
        if (intent.action == ACTION_SET_EXACT) {
            buildNotification(context, "Reminding", convertDate(timeInMillis))
        }
        val notificationIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("LINK")
        }
    }

    //building notification with io.karn:notify library
    private fun buildNotification(context: Context, title: String, message: String) {
        Notify
            .with(context)
            .content {
                this.title = title
                text = "You've set remind time at: - $message"
            }
            .show()
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}