package com.example.studybuddy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Lắng nghe các sự kiện báo thức và hiển thị thông báo.
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val courseName = intent.getStringExtra("courseName") ?: "Unknown Course"
        val startTime = intent.getStringExtra("startTime") ?: "Unknown Time"
        val notificationId = intent.getIntExtra("notificationId", 0)

        val notificationService = NotificationService.getInstance()
        notificationService.pushNotification(
            title = "Reminder: $courseName",
            message = "Sắp $startTime, tới giờ học rồi!",
            notificationId = notificationId
        )
    }
}
