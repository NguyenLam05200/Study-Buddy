package com.example.studybuddy.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.studybuddy.data.local.model.CourseModel
import java.util.Calendar
import java.util.concurrent.ConcurrentHashMap

object NotificationScheduler {
    private val scheduledNotifications = ConcurrentHashMap<Int, Long>()

    fun scheduleNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String,
        channelId: String,
        triggerAtMillis: Long // new Date(triggerAtMillis) của thằng này phải bằng local time thì mới push Noti
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
            putExtra("channelId", channelId)
            putExtra("notificationId", notificationId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
        // Lưu vào danh sách các thông báo đã lên lịch
        scheduledNotifications[notificationId] = triggerAtMillis
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            scheduledNotifications.remove(notificationId)
        }
    }

    fun getScheduledNotifications(context: Context): Map<Int, Long> {
        return scheduledNotifications
    }

}

