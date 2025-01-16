package com.example.studybuddy.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studybuddy.data.local.model.CourseModel

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY startTime ASC")
    fun getAllCourses(): LiveData<List<CourseModel>>

    @Query("SELECT * FROM courses ORDER BY startTime ASC")
    suspend fun getAllCoursesSync(): List<CourseModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseModel)

    @Delete
    suspend fun deleteCourse(course: CourseModel)

    @Query("DELETE FROM courses WHERE id = :courseId")
    suspend fun deleteCourseById(courseId: Int)

    @Update
    suspend fun updateCourse(course: CourseModel)

    @Query("SELECT COUNT(*) FROM courses")
    fun getCourseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseModel>)
}
