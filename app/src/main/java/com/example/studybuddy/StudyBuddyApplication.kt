package com.example.studybuddy

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.studybuddy.services.NotificationService

class StudyBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        checkAndRequestExactAlarmPermission()

        // Khởi tạo NotificationService
        NotificationService.init(this)

        // Kiểm tra và gửi thông báo chào mừng nếu cần
        showWelcomeNotificationIfFirstLaunch()
    }
    private fun checkAndRequestExactAlarmPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val alarmManager =
                getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Điều hướng người dùng đến trang cấp quyền
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    private fun showWelcomeNotificationIfFirstLaunch() {
        val preferences = getSharedPreferences("study_buddy_prefs", Context.MODE_PRIVATE)
        val isFirstLaunch = preferences.getBoolean("is_first_launch", true)

        if (isFirstLaunch) {
            // Gửi thông báo chào mừng
            val notificationService = NotificationService.getInstance()

            val title = getString(R.string.welcome_title)
            val message = getString(R.string.welcome_message)

            notificationService.pushNotification(
                title = title,
                message = message,
                notificationIdKey = "welcome_notification".hashCode(),
                channelId = NotificationService.GENERAL_NOTIFICATIONS_CHANNEL
            )

            // Đánh dấu đã hiển thị thông báo chào mừng
            preferences.edit().putBoolean("is_first_launch", false).apply()
        }
    }
}
