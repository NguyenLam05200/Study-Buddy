package com.example.studybuddy.data.repository

import com.example.studybuddy.data.local.model.CourseModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
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

    suspend fun addCourses(courses: List<CourseModel>) {
        realm.write {
            courses.forEach { copyToRealm(it) }
        }
    }

    suspend fun addCourse(course: CourseModel) {
        realm.write {
            copyToRealm(course)
        }
    }

    suspend fun deleteCourse(course: CourseModel) {
        realm.write {
            findLatest(course)?.let { delete(it) }
        }
    }

    suspend fun updateCourse(course: CourseModel) {
        realm.write {
            findLatest(course)?.apply {
                name = course.name
                dayOfWeek = course.dayOfWeek
                startTime = course.startTime
                endTime = course.endTime
                startDate = course.startDate
                endDate = course.endDate
                hasReminder = course.hasReminder
                room = course.room
            }
        }
    }
}

