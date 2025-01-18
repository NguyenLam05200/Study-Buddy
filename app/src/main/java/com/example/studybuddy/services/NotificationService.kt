package com.example.studybuddy.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.studybuddy.R

// Dịch vụ chính để quản lý việc hiển thị thông báo.
class NotificationService private constructor(context: Context) {
    companion object {
        const val CHANNEL_ID = "course_reminder_channel"
        private lateinit var instance: NotificationService

        fun init(applicationContext: Context) {
            instance = NotificationService(applicationContext)
        }

        fun getInstance(): NotificationService {
            if (!::instance.isInitialized) {
                throw IllegalStateException("NotificationService must be initialized in Application class")
            }
            return instance
        }
    }

    private val appContext = context.applicationContext

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channelName = "Course Reminders"
        val descriptionText = "Reminders for your upcoming courses"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Hàm hiển thị thông báo
    fun pushNotification(title: String, message: String, notificationId: Int) {
        // Kiểm tra quyền POST_NOTIFICATIONS
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (appContext.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                // Log hoặc xử lý nếu quyền chưa được cấp
                return
            }
        }

        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.school) // Thay bằng icon của bạn
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(appContext).notify(notificationId, notification)

    }
}
