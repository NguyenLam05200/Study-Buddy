package com.example.studybuddy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.utilities.reminderCourseNoti
import io.realm.kotlin.ext.query

// Đặt lại các alarm khi thiết bị khởi động lại.
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) {
                Log.e("BootReceiver", "Exact alarm permission not granted.")
                return
            }

            val realm = DatabaseProvider.getDatabase()
            val courses = realm.query<CourseModel>("hasReminder == true").find()
            for (course in courses) {
                reminderCourseNoti(context, course)
            }
        }
    }
}
