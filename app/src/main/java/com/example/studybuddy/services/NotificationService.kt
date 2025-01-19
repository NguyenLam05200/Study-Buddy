package com.example.studybuddy.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.studybuddy.R

// Dịch vụ chính để quản lý việc hiển thị thông báo.
class NotificationService private constructor(context: Context) {
    companion object {
        const val COURSE_REMINDER_CHANNEL = "course_reminder_channel"
        const val GENERAL_NOTIFICATIONS_CHANNEL = "general_notifications_channel"
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
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val courseReminderChannel = NotificationChannel(
            COURSE_REMINDER_CHANNEL, "Course Reminders", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminders for your upcoming courses"
        }

        val generalNotificationsChannel = NotificationChannel(
            GENERAL_NOTIFICATIONS_CHANNEL, "General Notifications", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "General notifications for the app"
        }

        val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(courseReminderChannel)
        notificationManager.createNotificationChannel(generalNotificationsChannel)
    }

    fun pushNotification(
        title: String, message: String, notificationIdKey: String, channelId: String
    ) {
        // Kiểm tra quyền POST_NOTIFICATIONS
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (appContext.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Log hoặc xử lý nếu quyền chưa được cấp
                return
            }
        }
        val notificationId = notificationIdKey.hashCode() // Chuyển String thành Int ID


        // Tạo icon lớn đầy màu sắc
        val largeIcon = BitmapFactory.decodeResource(appContext.resources, R.drawable.ic_logo_app)

        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.ic_notification_small) // Icon trắng đơn sắc
            .setLargeIcon(largeIcon) // Icon đầy màu sắc
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
//        val notification =
//            NotificationCompat.Builder(appContext, channelId).setSmallIcon(R.drawable.ic_logo_app) // Thay bằng icon của bạn
//                .setContentTitle(title).setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true).build()

        NotificationManagerCompat.from(appContext).notify(notificationId, notification)
    }
}
