package com.example.studybuddy.data.repository

import android.util.Log
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.CourseModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class CourseRepository(private val realm: Realm) {

    fun getAllCourses(): Flow<List<CourseModel>> {
        return realm.query<CourseModel>().asFlow().map { it.list }
    }

    suspend fun isEmpty(): Boolean {
        return realm.query<CourseModel>().count().find() == 0L
    }

    suspend fun getCourseById(courseId: Int): CourseModel? {
        return realm.query<CourseModel>("id == $0", courseId).first().find()
    }


    suspend fun addCourses(courses: List<CourseModel>) {
        realm.write {
            var nextId = DatabaseProvider.generateAutoIncrementId() // Lấy ID bắt đầu

            courses.forEach { course ->
                course.id = nextId // Gán ID tự động
                copyToRealm(course)
                nextId++ // Tăng ID cho lần tiếp theo
            }
        }
    }

    suspend fun addCourse(course: CourseModel) {
        realm.write {
            course.id = DatabaseProvider.generateAutoIncrementId() // Tạo ID tự động
            copyToRealm(course)
        }
    }


    suspend fun deleteCourse(course: CourseModel) {
        realm.write {
            findLatest(course)?.let { delete(it) }
        }
    }

    suspend fun updateCourse(course: CourseModel) {
        try {
            realm.write {
                // Query the managed object from Realm
                val managedCourse = query<CourseModel>("id == $0", course.id).first().find()
                if (managedCourse != null) {
                    managedCourse.apply {
                        name = course.name
                        dayOfWeek = course.dayOfWeek
                        startTime = course.startTime
                        endTime = course.endTime
                        startDate = course.startDate
                        endDate = course.endDate
                        hasReminder = course.hasReminder
                        room = course.room
                    }
                } else {
                    Log.e("____Repo", "updateCourse failed: Course not found with id ${course.id}")
                }
            }
        } catch (e: Exception) {
            Log.e("____RepoError", "updateCourse failed: ${e.message}", e)
            throw e
        }
    }

    fun logAllCourses() {
        val courses = realm.query<CourseModel>().find()
        courses.forEach {
            Log.d("____Repo", "Course: ${it.name}, startTime: ${it.startTime}, currentTime: ${System.currentTimeMillis()}")
        }
    }

    suspend fun getUpcomingCourses(limit: Int): List<CourseModel> {
        val courses = realm.query<CourseModel>().find()

        return courses
    }
}

