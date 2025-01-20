package com.example.studybuddy.utilities

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.studybuddy.data.local.DateUtils
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.local.model.DeadlineModel
import com.example.studybuddy.services.NotificationScheduler
import com.example.studybuddy.services.NotificationService
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

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
    val nextTimeClass = calculateNextClassTimestamp(course.startTime, course.dayOfWeek)

    if (nextTimeClass < course.startDate + course.startTime || nextTimeClass > course.endDate + course.startTime) {
        return
    }

    NotificationScheduler.scheduleNotification(
        context = context,
        notificationId = course.id,
        title = "Reminder: ${course.name}",
        message = "Sắp ${formatTime(course.startTime)}, tới giờ học rồi!",
        channelId = NotificationService.COURSE_REMINDER_CHANNEL,
        triggerAtMillis = nextTimeClass - 5 * 60 * 1000 // 5 phút trước giờ học
    )
}

fun reminderDeadlineNoti(context: Context, deadline: DeadlineModel) {
    if (deadline.dueDate < System.currentTimeMillis()) {
        return
    }

    NotificationScheduler.scheduleNotification(
        context = context,
        notificationId = deadline.id,
        title = "Deadline: ${deadline.name}",
        message = "Sắp ${formatTime(deadline.dueDate)}, trễ deadline rồi!",
        channelId = NotificationService.GENERAL_NOTIFICATIONS_CHANNEL,
        triggerAtMillis = deadline.dueDate - 5 * 60 * 1000 // 5 phút trước giờ học
    )
}

fun calculateNextClassTimestamp(startTime: Long, dayOfWeek: Int): Long {
    val calendar = Calendar.getInstance()

    // Lấy ngày hiện tại
    val currentDayOfWeek = convertCalendarDayToDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
    val targetDayOfWeek = dayOfWeek // Calendar.DAY_OF_WEEK starts from 1 (Sunday)

    // Tính số ngày cần thêm để đến đúng thứ
    var daysToAdd = targetDayOfWeek - currentDayOfWeek

    val startDate = Date(startTime)
    val startTimeCalendar = Calendar.getInstance()
    startTimeCalendar.time = startDate

    val startHour = startTimeCalendar.get(Calendar.HOUR_OF_DAY)
    val startMinute = startTimeCalendar.get(Calendar.MINUTE)
    val startSecond = startTimeCalendar.get(Calendar.SECOND)

    // Lấy giờ, phút, giây hiện tại (local time)
    val localCalendar = Calendar.getInstance()
    val currentHour = localCalendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = localCalendar.get(Calendar.MINUTE)
    val currentSecond = localCalendar.get(Calendar.SECOND)

    // So sánh giờ, phút, giây
    val isTimePassed = when {
        currentHour > startHour -> true
        currentHour == startHour && currentMinute > startMinute -> true
        currentHour == startHour && currentMinute == startMinute && currentSecond >= startSecond -> true
        else -> false
    }

    if (daysToAdd < 0 || (daysToAdd == 0 && isTimePassed)) {
        daysToAdd += 7 // Nếu đã qua ngày học hoặc giờ học, chuyển sang tuần tiếp theo
    }

    // Cộng số ngày cần thiết để tới đúng ngày học
    calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)
        calendar.set(Calendar.HOUR_OF_DAY, startHour)
    calendar.set(Calendar.MINUTE, startMinute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}

fun convertCalendarDayToDayOfWeek(calendarDay: Int): Int {
    return when (calendarDay) {
        Calendar.SUNDAY -> 7 // SUNDAY của Calendar -> 7 (DayOfWeek)
        else -> calendarDay - 1 // Các ngày khác: MONDAY (2) -> 1, TUESDAY (3) -> 2, ...
    }
}


