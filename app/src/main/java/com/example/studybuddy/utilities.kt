package com.example.studybuddy.utilities

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.studybuddy.data.local.DateUtils
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.services.NotificationScheduler
import com.example.studybuddy.services.NotificationService

object CONF {
    var sharedPreferences: SharedPreferences? = null

    val languages = listOf(
        "English", "Tiếng Việt"
    )

    val language_codes = listOf(
        "en", "vi"
    )

    val fontsizes = listOf(
        "13dp", "16dp"
    )

    val datetime_formats = listOf(
        "dd/mm/yyyy", "mm/dd/yyyy"
    )

    val SWITCH_BUTTON_KEY = "switch"
    val LANGUAGE_KEY = "language"
    val PREF_KEY = "pref"
}

object PreferencesManager {
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize the shared preferences in the application context (this will be done once)
    fun initialize(context: Context) {
        sharedPreferences = context.applicationContext.getSharedPreferences(CONF.PREF_KEY, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun getInt(key: String, defValue: Int = -1): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun getString(key: String, defValue: String? = null): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
}

fun showInfoDialog(context: Context, title: String = "Default", message: String, ok_button_text: String = "OK") {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton(ok_button_text) { dialog: DialogInterface, which: Int ->
        dialog.dismiss()
    }

    // Optional: Set a negative button, like a cancel button
//        builder.setNegativeButton("Cancel") { dialog, which ->
//            dialog.dismiss()  // Close the dialog
//        }

    val dialog = builder.create()
    dialog.show()
}

// Toast chỉ có thể được gọi trên UI thread (main thread)
fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        // Đang ở UI thread
        Toast.makeText(context, message, duration).show()
    } else {
        // Chuyển sang UI thread
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, duration).show()
        }
    }
}


// Format thời gian
fun formatTime(value: Long): String {
    return DateUtils.formatTimestamp(value, "HH:mm")
}

// Format ngày
fun formatDate(value: Long): String {
    return DateUtils.formatTimestamp(value, "dd/MM/yyyy")
}

fun formatTimeRange(startTime: Long, endTime: Long): String {
    return "${formatTime(startTime)} - ${formatTime(endTime)}"
}

fun formatDateRange(startDate: Long, endDate: Long): String {
    return "${formatDate(startDate)} - ${formatDate(endDate)}"
}


fun reminderCourseNoti(context: Context, course: CourseModel) {
    NotificationScheduler.scheduleNotification(
        context = context,
        notificationId = course.id,
        title = "Reminder: ${course.name}",
        message = "Sắp ${formatTime(course.startTime)}, tới giờ học rồi!",
        channelId = NotificationService.COURSE_REMINDER_CHANNEL,
        triggerAtMillis = course.startTime - 5 * 60 * 1000 // 5 phút trước giờ học
    )
}