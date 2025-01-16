package com.example.studybuddy.ui.course

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.repository.CourseRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.find
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val defaultCourses = listOf(
    CourseModel().apply {
        id = 1L
        name = "Lập trình Mobile"
        dayOfWeek = "Monday"
        startTime = RealmInstant.from(1700000000, 0)
        endTime = RealmInstant.from(1700003600, 0)
        startDate = RealmInstant.now()
        endDate = RealmInstant.from(1700000000 + 7776000, 0) // +90 days
        hasReminder = true
        room = "D211"
    },
    CourseModel().apply {
        id = 2L
        name = "Data Science"
        dayOfWeek = "Wednesday"
        startTime = RealmInstant.from(1700100000, 0)
        endTime = RealmInstant.from(1700103600, 0)
        startDate = RealmInstant.now()
        endDate = RealmInstant.from(1700100000 + 7776000, 0) // +90 days
        hasReminder = true
        room = "C101"
    }
)

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {

    // Replace direct Realm query with repository
    val courses: Flow<List<CourseModel>> = repository.getAllCourses()

    fun initializeDefaultCourses() {
        viewModelScope.launch {
            if (repository.isEmpty()) { // Check if repository is empty
                repository.addCourses(defaultCourses)
            }
        }
    }

    fun addCourse(course: CourseModel) {
        viewModelScope.launch {
            repository.addCourse(course)
        }
    }

    fun deleteCourse(course: CourseModel) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }

    fun updateCourse(course: CourseModel) {
        viewModelScope.launch {
            repository.updateCourse(course)
        }
    }
}
