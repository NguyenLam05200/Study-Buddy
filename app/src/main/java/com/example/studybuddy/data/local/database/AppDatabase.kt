package com.example.studybuddy.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studybuddy.data.local.dao.CourseDao
import com.example.studybuddy.data.local.model.CourseModel

@Database(entities = [CourseModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
