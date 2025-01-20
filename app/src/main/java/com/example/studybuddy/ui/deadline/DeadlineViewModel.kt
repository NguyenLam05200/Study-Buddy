package com.example.studybuddy.ui.deadline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.model.DeadlineModel
import com.example.studybuddy.data.repository.DeadlineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.DayOfWeek

val defaultDeadlines = listOf(
    DeadlineModel().apply {
        id = 1
        name = "Vấn đáp cuối kỳ Mobile"
        time = 1737358800000L
        dueDate = 1737368400000L
        courseId = 1
        courseName = "Lập trình Mobile"
    },
    DeadlineModel().apply {
        id = 2
        name = "Thi cuối kỳ Web"
        time = 1726790400000L
        dueDate = 1737368400000L
        courseId = 2
        courseName = "Lập trình Web"
    }
)

class DeadlineViewModel(private val repository: DeadlineRepository) : ViewModel() {

    // Replace direct Realm query with repository
    val deadlines: Flow<List<DeadlineModel>> = repository.getAllDeadlines()

    fun initializeDefaultDeadlines() {
        viewModelScope.launch {
            if (repository.isEmpty()) { // Check if repository is empty
                repository.addDeadlines(defaultDeadlines)
            }
        }
    }

    fun addDeadline(Deadline: DeadlineModel) {
        viewModelScope.launch {
            repository.addDeadline(Deadline)
        }
    }

    fun deleteDeadline(Deadline: DeadlineModel) {
        viewModelScope.launch {
            repository.deleteDeadline(Deadline)
        }
    }

    fun updateDeadline(Deadline: DeadlineModel) {
        viewModelScope.launch {
            repository.updateDeadline(Deadline)
        }
    }
}
