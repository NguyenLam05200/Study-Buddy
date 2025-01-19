package com.example.studybuddy.ui.todolist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.model.Task
import com.example.studybuddy.data.repository.TaskRepository
import io.realm.kotlin.ext.isManaged
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    // Lấy tất cả các task từ database
    fun initialize() {
        viewModelScope.launch {
            val taskList = repository.getAllTasks()
            _tasks.postValue(taskList)
        }
    }

    // Thêm task mới
    fun add(task: Task, callback: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addTask(task)
            callback()
            refreshTasks()
        }
    }

    // Xóa một task
    fun remove(task: Task, callback: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteTask(task)
            callback()
            refreshTasks()
        }
    }

    // Cập nhật task
    fun update(position: Int, uuid: String, updatedTask: Task, callback: () -> Unit = {}) {
        viewModelScope.launch {
            repository.updateTask(uuid, updatedTask)
            refreshTasks()
            callback()
        }
    }

    // Lấy một task cụ thể
    fun getTask(position: Int): Task? {
        return _tasks.value?.getOrNull(position)
    }

    // Lấy tổng số task
    fun getSize(): Int {
        return _tasks.value?.size ?: 0
    }

    private suspend fun refreshTasks() {
        _tasks.postValue(repository.getAllTasks())
    }

    fun updateLiveData(updatedTasks: List<Task>) {
        _tasks.value = updatedTasks
    }
}
