package com.example.studybuddy.ui.course

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Calendar

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
    val courses: LiveData<List<CourseModel>> = repository.allCourses

    fun addCourse(course: CourseModel) {
        viewModelScope.launch {
            repository.insertCourse(course)
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

    fun initializeDefaultCourses() {
        viewModelScope.launch {
            try {
                val curTimestamp = DateUtils.getCurrentTimestamp();
                val fake3months = curTimestamp + 86400000 * 90;
                val defaultCourses = listOf(
                    CourseModel(
                        name = "Lập trình Mobile",
                        dayOfWeek = Calendar.MONDAY,
                        startTime = 1700000000000L,
                        endTime = 1700003600000L,
                        startDate = curTimestamp,
                        endDate = fake3months,
                        hasReminder = true,
                        room = "D211"
                    )
                )
                val existingCourses = repository.getAllCoursesSync()
                if (existingCourses.isEmpty()) {
                    repository.insertCourses(defaultCourses)
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error initializing default courses: ${e.message}")
            }
        }


    }

}
