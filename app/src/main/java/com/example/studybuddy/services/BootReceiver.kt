package com.example.studybuddy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.CourseModel
import io.realm.kotlin.ext.query
import java.util.Calendar

// Đặt lại các alarm khi thiết bị khởi động lại.
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val realm = DatabaseProvider.getDatabase()
            val courses = realm.query<CourseModel>("hasReminder == true").find()
            for (course in courses) {
                NotificationScheduler.scheduleNotification(context, course)
            }
        }
    }
}
