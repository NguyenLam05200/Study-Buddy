package com.example.studybuddy.data.local.model


import io.realm.kotlin.types.annotations.PrimaryKey
import io.realm.kotlin.types.RealmInstant
import com.example.studybuddy.data.local.DateUtils
import io.realm.kotlin.types.RealmObject

class CourseModel : RealmObject {
    @PrimaryKey
    var id: Long = 0
    var name: String = ""
    var dayOfWeek: String = ""
    var startTime: RealmInstant = RealmInstant.now()
    var endTime: RealmInstant = RealmInstant.now()
    var startDate: RealmInstant = RealmInstant.now()
    var endDate: RealmInstant = RealmInstant.now()
    var hasReminder: Boolean = false
    var room: String? = null

    fun formatTimeRange(): String {
        return "${DateUtils.formatTimestamp(startTime.epochSeconds, "hh:mm a")} - ${DateUtils.formatTimestamp(endTime.epochSeconds, "hh:mm a")}"
    }

    fun formatDateRange(): String {
        return "${DateUtils.formatTimestamp(startDate.epochSeconds, "dd/MM/yyyy")} - ${DateUtils.formatTimestamp(endDate.epochSeconds, "dd/MM/yyyy")}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CourseModel

        // Nếu `this` hoặc `other` không phải là Realm object thì so sánh như bình thường
        if (!this.isManagedRealmObject() || !other.isManagedRealmObject()) {
            return id == other.id &&
                    name == other.name &&
                    dayOfWeek == other.dayOfWeek &&
                    startTime == other.startTime &&
                    endTime == other.endTime &&
                    startDate == other.startDate &&
                    endDate == other.endDate &&
                    hasReminder == other.hasReminder &&
                    room == other.room
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
        result = 31 * result + dayOfWeek.hashCode()
        result = 31 * result + startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + hasReminder.hashCode()
        result = 31 * result + (room?.hashCode() ?: 0)
        return result
    }
}

