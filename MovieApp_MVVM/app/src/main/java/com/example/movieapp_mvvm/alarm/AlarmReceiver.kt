package com.example.movieapp_mvvm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.example.movieapp_mvvm.util.Constants
import io.karn.notify.Notify

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeMills = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        if (intent.action == Constants.ACTION_SET_EXACT_ALARM) {
            buildNotification(context, "Set exact time", convertDate(timeMills))
        }
    }

    //TODO
    // add standart Notification
    fun buildNotification(context: Context, title: String, message: String) {
        Notify
            .with(context)
            .content {
                this.title = title
                this.text = "Notification message: $message"
            }
            .show()
    }

    fun convertDate(timeMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeMillis).toString()


}