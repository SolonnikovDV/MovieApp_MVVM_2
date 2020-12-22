package com.example.movieapp_mvvm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.movieapp_mvvm.util.Constants
import com.example.movieapp_mvvm.util.RandomIntUtil

class AlarmService(val context: Context) {
    val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setExactAlarm(timeInMills: Long) {
        setAlarm(
            timeInMills,
            getPendingIntent(
            getIntent().apply {
                action = Constants.ACTION_SET_EXACT_ALARM
                putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMills)
            }
        ))
    }

    fun setAlarm(timeInMills: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent
                )
            }
        }
    }

    fun getIntent() = Intent(context, AlarmService::class.java)

    fun getPendingIntent(intent: Intent): PendingIntent = PendingIntent.getBroadcast(
        context,
        RandomIntUtil.getRandomInt(),
        intent,
        PendingIntent.FLAG_CANCEL_CURRENT
    )
}