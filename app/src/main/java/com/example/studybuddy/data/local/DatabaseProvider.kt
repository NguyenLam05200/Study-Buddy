package com.example.studybuddy.data.local

import android.content.Context
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.ui.todolist.data.Task
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Sử dụng Schema gì muốn dăng ký Realm Database thì bỏ vào đây
object RealmSchemas {
    val SCHEMA = setOf(
        CourseModel::class,
        Task::class
//        QuizModel::class,
//        SchedulerModel::class
    )
}

object DatabaseProvider {
    private var realm: Realm? = null

    fun getDatabase(context: Context): Realm {
        if (realm == null) {
            val config = RealmConfiguration.Builder(schema = RealmSchemas.SCHEMA)
                .name("study_buddy.realm") // Tên file Realm
                .deleteRealmIfMigrationNeeded() // Xóa dữ liệu nếu có thay đổi schema
                .build()
            realm = Realm.open(config)
        }
        return realm!!
    }
}

object DateUtils {
    private const val DEFAULT_PATTERN = "hh:mm dd/MM/yyyy"

    // Lấy timestamp hiện tại
    fun getCurrentTimestamp(): Long {
        return Instant.now().toEpochMilli()
    }

    // Định dạng timestamp theo format
    fun formatTimestamp(
        timestamp: Long,
        pattern: String = DEFAULT_PATTERN,
        locale: Locale = Locale.getDefault()
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        val dateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        return formatter.format(dateTime)
    }


}

