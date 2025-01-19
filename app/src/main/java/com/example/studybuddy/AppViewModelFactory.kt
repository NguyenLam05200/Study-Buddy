package com.example.studybuddy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.data.repository.TaskRepository
import com.example.studybuddy.ui.course.CourseViewModel
import com.example.studybuddy.ui.course.ManageCourseViewModel
import com.example.studybuddy.ui.home.home_vm
import com.example.studybuddy.ui.todolist.TaskViewModel

class CourseViewModelFactory(
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
            modelClass.isAssignableFrom(home_vm::class.java) -> {
                home_vm(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

class TaskViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TaskViewModel::class.java) -> {
                TaskViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
