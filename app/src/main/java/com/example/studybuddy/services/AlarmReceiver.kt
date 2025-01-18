package com.example.studybuddy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.utilities.formatTime

// Lắng nghe các sự kiện báo thức và hiển thị thông báo.
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val courseName = intent.getStringExtra("courseName") ?: "Unknown Course"
        val startTime = intent.getLongExtra("startTime", 0)
        val notificationId = intent.getIntExtra("notificationId", 0)

        val notificationService = NotificationService.getInstance()
        notificationService.pushNotification(
            title = "Reminder: $courseName",
            message = "Sắp ${formatTime(startTime)}, tới giờ học rồi!",
            notificationId = notificationId
        )
    }
}
