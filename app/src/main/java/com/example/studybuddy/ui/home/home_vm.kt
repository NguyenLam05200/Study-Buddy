package com.example.studybuddy.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.repository.CourseRepository
import kotlinx.coroutines.launch

class home_vm(private val repository: CourseRepository) : ViewModel() {

    private val _upcomingCourses = MutableLiveData<List<CourseModel>>()
    val upcomingCourses: LiveData<List<CourseModel>> get() = _upcomingCourses
    fun fetchUpcomingCourses(limit: Int = 3) {
        viewModelScope.launch {
            repository.logAllCourses() // Log toàn bộ dữ liệu
            val upcoming = repository.getUpcomingCourses(limit)
            Log.d("____VM", "fetchUpcomingCourses: Found ${upcoming.size} upcoming courses")
            _upcomingCourses.postValue(upcoming)
        }
    }


    fun deleteCourse(course: CourseModel) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }
}
