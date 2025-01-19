package com.example.studybuddy

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.studybuddy.services.NotificationService
import org.mongodb.kbson.BuildConfig
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.studybuddy.services.NotificationScheduler


class StudyBuddyApplication : Application() {
    private val handler = Handler(Looper.getMainLooper())
    private val logJobRunnable = object : Runnable {
        override fun run() {
            // Gọi hàm log các thông báo đã được lên lịch
            logScheduledNotifications()
            handler.postDelayed(this, 30 * 1000L) // Lặp lại sau mỗi 30 giây
        }
    }
    override fun onCreate() {
        super.onCreate()

        checkAndRequestExactAlarmPermission()

        // Khởi tạo NotificationService
        NotificationService.init(this)

        // Kiểm tra và gửi thông báo chào mừng nếu cần
        showWelcomeNotificationIfFirstLaunch()

        startLoggingScheduledNotifications()
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


    private fun startLoggingScheduledNotifications() {
        handler.post(logJobRunnable)
    }

    private fun logScheduledNotifications() {
        Log.d("ScheduledNoti", "Logging scheduled notifications...")
        // Lấy danh sách các thông báo đã lên lịch
        val context = this
        val scheduledNotifications = NotificationScheduler.getScheduledNotifications(context)

        // Log từng thông báo
        for ((notificationId, triggerTime) in scheduledNotifications) {
            val timeString = java.text.SimpleDateFormat(
                "HH:mm:ss dd/MM/yyyy",
                java.util.Locale.getDefault()
            ).format(triggerTime)
            Log.d("ScheduledNoti", "Notification ID: $notificationId | Scheduled Time: $timeString | Scheduled Message: ${scheduledNotifications[notificationId]}")
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        // Hủy job khi ứng dụng bị tắt
        handler.removeCallbacks(logJobRunnable)
    }


    private fun showWelcomeNotificationIfFirstLaunch() {
        val preferences = getSharedPreferences("study_buddy_prefs", Context.MODE_PRIVATE)

        // Đặt lại cờ khi ở chế độ debug
        if (BuildConfig.DEBUG) {
            preferences.edit().putBoolean("is_first_launch", true).apply()
        }

        val isFirstLaunch = preferences.getBoolean("is_first_launch", true)

        if (isFirstLaunch) {
            // Gửi thông báo chào mừng
            val notificationService = NotificationService.getInstance()

            val title = getString(R.string.welcome_title)
            val message = getString(R.string.welcome_message)

            notificationService.pushNotification(
                title = title,
                message = message,
                notificationIdKey = "welcome_notification",
                channelId = NotificationService.GENERAL_NOTIFICATIONS_CHANNEL
            )

            // Đánh dấu đã hiển thị thông báo chào mừng
            preferences.edit().putBoolean("is_first_launch", false).apply()
        }
    }

}
