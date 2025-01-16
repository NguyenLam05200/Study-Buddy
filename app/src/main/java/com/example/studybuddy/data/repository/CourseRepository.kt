package com.example.studybuddy.data.repository

import androidx.lifecycle.LiveData
import com.example.studybuddy.data.local.dao.CourseDao
import com.example.studybuddy.data.local.model.CourseModel

class CourseRepository(private val courseDao: CourseDao) {

    val allCourses: LiveData<List<CourseModel>> = courseDao.getAllCourses()

    suspend fun insertCourse(course: CourseModel) {
        courseDao.insertCourse(course)
    }

    suspend fun deleteCourse(course: CourseModel) {
        courseDao.deleteCourse(course)
    }

    suspend fun updateCourse(course: CourseModel) {
        courseDao.updateCourse(course)
    }

    suspend fun insertCourses(courses: List<CourseModel>) {
        courseDao.insertCourses(courses)
    }

    suspend fun getAllCoursesSync(): List<CourseModel> {
        return courseDao.getAllCoursesSync()
    }
}


