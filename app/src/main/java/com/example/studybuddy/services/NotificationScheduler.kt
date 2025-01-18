package com.example.studybuddy.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.studybuddy.data.local.model.CourseModel
import java.util.Calendar

object NotificationScheduler {

    fun scheduleNotification(context: Context, course: CourseModel) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("courseName", course.name)
            putExtra("startTime", course.startTime.toString()) // Format nếu cần
            putExtra("notificationId", course.id.toInt())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            course.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = course.startTime - 5 * 60 * 1000 // 5 phút trước startTime
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
