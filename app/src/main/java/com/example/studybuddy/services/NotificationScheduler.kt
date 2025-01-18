package com.example.studybuddy.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.studybuddy.data.local.model.CourseModel
import java.util.Calendar
object NotificationScheduler {
    fun scheduleNotification(
        context: Context,
        courseId: Long,
        courseName: String,
        startTimeMillis: Long
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("courseName", courseName)
            putExtra("startTime", startTimeMillis) // Pass as Long
            putExtra("notificationId", courseId.toInt()) // Unique notification ID
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            courseId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            startTimeMillis - 5 * 60 * 1000, // 5 minutes before startTimeMillis
            pendingIntent
        )
    }


    fun cancelNotification(context: Context, courseId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            courseId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }
}

