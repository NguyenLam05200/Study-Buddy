package com.example.studybuddy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Lắng nghe các sự kiện báo thức và hiển thị thông báo.
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationType = intent.getStringExtra("notificationType") ?: "general"
        val title = intent.getStringExtra("title") ?: "Notification"
        val message = intent.getStringExtra("message") ?: "You have a reminder"
        val notificationId = intent.getIntExtra("notificationId", 0)
        val channelId = intent.getStringExtra("channelId") ?: NotificationService.GENERAL_NOTIFICATIONS_CHANNEL

        val notificationService = NotificationService.getInstance()
        notificationService.pushNotification(
            title = title,
            message = message,
            notificationIdKey = notificationId,
            channelId = channelId
        )
    }
}
