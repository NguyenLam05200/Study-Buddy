package com.example.studybuddy.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.studybuddy.utils.DateUtils
import java.util.Locale

@Entity(tableName = "courses")
data class CourseModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String, // Tên môn học
    val dayOfWeek: Int, // Thứ trong tuần (Calendar.MONDAY, ...)
    val startTime: Long, // Thời gian bắt đầu (timestamp) ví dụ 8h am
    val endTime: Long, // Thời gian kết thúc (timestamp) ví dụ 10h am
    val startDate: Long, // Ngày bắt đầu khóa học (timestamp) ví dụ 21/09/2024
    val endDate: Long, // Ngày kết thúc khóa học (timestamp) ví dụ 21/12/2024
    val hasReminder: Boolean = false, // Có bật noti nhắc nhở không
    val room: String? // Phòng học
) {
    @Ignore
    fun formatDayOfWeek(locale: Locale = Locale.getDefault()): String {
        val daysOfWeek = if (locale.language == "vi") {
            listOf("Chủ Nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7")
        } else {
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        }
        return daysOfWeek[dayOfWeek - 1] // dayOfWeek starts from Calendar.SUNDAY = 1
    }

    @Ignore
    fun formatDateRange(): String {
        return "${DateUtils.formatTimestamp(startDate, "dd/MM/yyyy")} - ${
            DateUtils.formatTimestamp(
                endDate,
                "dd/MM/yyyy"
            )
        }"
    }

    @Ignore
    fun formatTimeRange(): String {
        return "${DateUtils.formatTimestamp(startTime, "hh:mm a")}, ${formatDayOfWeek()}"
    }
}
