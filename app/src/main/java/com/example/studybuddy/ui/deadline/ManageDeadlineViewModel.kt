package com.example.studybuddy.ui.deadline

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.local.model.DeadlineModel
import com.example.studybuddy.data.repository.DeadlineRepository
import com.example.studybuddy.services.NotificationScheduler
import com.example.studybuddy.utilities.reminderDeadlineNoti
import com.example.studybuddy.utilities.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageDeadlineViewModel(val repository: DeadlineRepository) : ViewModel() {
    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    // Lấy khóa học theo ID
    fun getDeadlineById(deadlineId: Int): LiveData<DeadlineModel?> {
        val liveData = MutableLiveData<DeadlineModel?>()
        viewModelScope.launch {
            liveData.postValue(repository.getDeadlineById(deadlineId))
        }
        return liveData
    }

    // Thêm mới khóa học
    fun addDeadline(deadline: DeadlineModel, context: Context) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.addDeadline(deadline)
                }
                Log.d("ManageDeadlineViewModel", "Added success: ${deadline.time}")

                withContext(Dispatchers.Main) {
                    _updateStatus.value = true // Cập nhật trạng thái thành công
                    showToast(context, "Deadline added successfully!")
                    if (deadline.hasReminder) {
                        reminderDeadlineNoti(context, deadline)
                    }
                }
            } catch (e: Exception) {
                Log.e("ManageDeadlineViewModel", "Failed to add new deadline: ${e.message}")
                _updateStatus.value = false // Cập nhật trạng thái thất bại
            }
        }


//        viewModelScope.launch {
//            repository.addDeadline(deadline)
//            showToast(context, "Deadline added successfully!")
//
//            if (deadline.hasReminder) {
//                reminderDeadlineNoti(context, deadline)
//            }
//        }
    }

    // Cập nhật khóa học
    fun updateDeadline(deadline: DeadlineModel, context: Context, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                deadline.updateDueDate()

                withContext(Dispatchers.IO) {
                    repository.updateDeadline(deadline)
                }
                withContext(Dispatchers.Main) {
                    showToast(context, "Deadline updated successfully!")
                    if (deadline.hasReminder) {
                        reminderDeadlineNoti(context, deadline)
                    } else {
                        NotificationScheduler.cancelNotification(context, deadline.id)
                    }
                    onComplete(true) // Báo hiệu thành công
                }
            } catch (e: Exception) {
                Log.e("ManageDeadlineViewModel", "Failed to update deadline: ${e.message}")
                onComplete(false) // Báo hiệu thất bại
            }
        }
    }
    fun getAllCourses(): List<CourseModel> {
        return repository.getAllCourses()
    }
}
