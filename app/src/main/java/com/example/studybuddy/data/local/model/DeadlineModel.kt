package com.example.studybuddy.data.local.model


import android.util.Log
import com.example.studybuddy.data.local.DateUtils
import com.example.studybuddy.utilities.formatDate
import com.example.studybuddy.utilities.formatTime
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.DayOfWeek

class DeadlineModel : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var time: Long = System.currentTimeMillis()
    var dueDate: Long = System.currentTimeMillis()
    var courseId: Int = 0 // Liên kết với ID của CourseModel
    var courseName: String = "" // Liên kết với ID của CourseModel
    var hasReminder: Boolean = true

    fun formatTimeDeadlineTime(): String {
        return "${formatTime(time)} ${formatDate(dueDate)}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DeadlineModel

        // Nếu `this` hoặc `other` không phải là Realm object thì so sánh như bình thường
        if (!this.isManagedRealmObject() || !other.isManagedRealmObject()) {
            return id == other.id &&
                    name == other.name &&
                    time == other.time &&
                    dueDate == other.dueDate &&
                    courseId == other.courseId &&
                    courseName == other.courseName &&
                    hasReminder == other.hasReminder
        }

        // Đối với RealmObject, so sánh bằng cách sử dụng chính Realm
        return this.id == other.id
    }

    private fun Any.isManagedRealmObject(): Boolean {
        return this is RealmObject
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + dueDate.hashCode()
        result = 31 * result + courseId.hashCode()
        result = 31 * result + courseName.hashCode()
        result = 31 * result + hasReminder.hashCode()
        return result
    }

    // Hàm cập nhật `dueDate` dựa trên `time`
    fun updateDueDate() {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = dueDate

        val timeCalendar = java.util.Calendar.getInstance()
        timeCalendar.timeInMillis = time

        // Lấy giờ và phút từ `time`
        val hour = timeCalendar.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = timeCalendar.get(java.util.Calendar.MINUTE)

        // Gán giờ và phút vào `dueDate`
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
        calendar.set(java.util.Calendar.MINUTE, minute)

        // Cập nhật `dueDate`
        dueDate = calendar.timeInMillis
    }

}