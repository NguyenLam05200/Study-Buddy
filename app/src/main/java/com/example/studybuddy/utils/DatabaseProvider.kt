package com.example.studybuddy.utils

import android.content.Context
import androidx.room.Room
import com.example.studybuddy.data.local.database.AppDatabase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "study_buddy_db"
            )
                .fallbackToDestructiveMigration() // Xóa toàn bộ dữ liệu nếu schema thay đổi
                .build()
            INSTANCE = instance
            instance
        }
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

