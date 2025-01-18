package com.example.studybuddy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.ui.course.CourseViewModel
import com.example.studybuddy.ui.course.ManageCourseViewModel

class AppViewModelFactory(
    private val repository: CourseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CourseViewModel::class.java) -> {
                CourseViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ManageCourseViewModel::class.java) -> {
                ManageCourseViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
