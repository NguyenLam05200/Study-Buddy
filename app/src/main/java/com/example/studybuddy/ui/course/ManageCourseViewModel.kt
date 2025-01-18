package com.example.studybuddy.ui.course

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.services.NotificationScheduler
import com.example.studybuddy.utilities.reminderCourseNoti
import com.example.studybuddy.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageCourseViewModel(val repository: CourseRepository) : ViewModel() {
    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    // Lấy khóa học theo ID
    fun getCourseById(courseId: Int): LiveData<CourseModel?> {
        val liveData = MutableLiveData<CourseModel?>()
        viewModelScope.launch {
            liveData.postValue(repository.getCourseById(courseId))
        }
        return liveData
    }

    // Thêm mới khóa học
    fun addCourse(course: CourseModel, context: Context) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.addCourse(course)
                }
                Log.d("ManageCourseViewModel", "Added success: ${course.startTime}")

                withContext(Dispatchers.Main) {
                    _updateStatus.value = true // Cập nhật trạng thái thành công
                    showToast(context, "Course added successfully!")
                    if (course.hasReminder) {
                        reminderCourseNoti(context, course)
                    }
                }
            } catch (e: Exception) {
                Log.e("ManageCourseViewModel", "Failed to add new course: ${e.message}")
                _updateStatus.value = false // Cập nhật trạng thái thất bại
            }
        }


//        viewModelScope.launch {
//            repository.addCourse(course)
//            showToast(context, "Course added successfully!")
//
//            if (course.hasReminder) {
//                reminderCourseNoti(context, course)
//            }
//        }
    }

    // Cập nhật khóa học
    fun updateCourse(course: CourseModel, context: Context, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.updateCourse(course)
                }
                withContext(Dispatchers.Main) {
                    showToast(context, "Course updated successfully!")
                    if (course.hasReminder) {
                        reminderCourseNoti(context, course)
                    } else {
                        NotificationScheduler.cancelNotification(context, course.id)
                    }
                    onComplete(true) // Báo hiệu thành công
                }
            } catch (e: Exception) {
                Log.e("ManageCourseViewModel", "Failed to update course: ${e.message}")
                onComplete(false) // Báo hiệu thất bại
            }
        }
    }


}
