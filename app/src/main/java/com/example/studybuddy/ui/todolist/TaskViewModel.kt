package com.example.studybuddy.ui.todolist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.ui.todolist.data.Task
import com.example.studybuddy.ui.todolist.data.TaskRepository
import io.realm.kotlin.ext.isManaged
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData<List<Task>>(emptyList())
    val tasks: LiveData<List<Task>> = _tasks

    fun initialize() {
        if (_tasks.value.isNullOrEmpty()) {
            viewModelScope.launch {
                if (!repository.isEmpty()) { // Check if repository is empty
                    val tasks = repository.getAll()

                    for (i in 0 until tasks.size) {
                        Log.d("TODOLIST", "Index ${i}: isManaged: ${tasks[i].isManaged()}")
                    }

                    setTasks(tasks)
                }
            }
        }
    }

    fun logAll() {
        for (i in 0 until tasks.value!!.size) {
            val task = tasks.value!![i]
            Log.d(
                "TODOLIST",
                "Index ${i}: isManaged: ${task.isManaged()}, ${task.uuid}, ${task.text}, ${task.isChecked}"
            )
        }
    }

    fun setTasks(newTasks: List<Task>) {
        _tasks.value = newTasks
    }

    fun getTask(pos: Int): Task? {
        return _tasks.value?.get(pos)
    }

    fun getSize(): Int {
        return _tasks.value?.size ?: 0
    }

    fun add(item: Task, callback: () -> Unit) {
        viewModelScope.launch {
            val managedItem = repository.add(item)
            Log.d("TODOLIST", "Adding in ViewModel: ${item.uuid}, ${item.text}, ${item.isChecked}")
            Log.d("TODOLIST", "Is Managed: ${managedItem.isManaged()}")

            val currentTasks = _tasks.value.orEmpty().toMutableList()
            currentTasks.add(managedItem)
            setTasks(currentTasks)

            // logAll()

            callback()
        }
    }

    fun remove(item: Task, callback: () -> Unit) {
        viewModelScope.launch {
            repository.remove(item)

            val updatedTasks = _tasks.value?.filter { it.uuid != item.uuid }.orEmpty() // filter out the removed item
            setTasks(updatedTasks)

            // logAll()

            callback()
        }
    }

    fun update(pos: Int, uuid: String, updated: Task, callback: () -> Unit) {
        viewModelScope.launch {
            repository.update(uuid, updated)

            val updatedTasks = _tasks.value?.let { tasks ->
                for (i in 0 until tasks.size)
                {
                    if (tasks[i].uuid == uuid)
                    {
                        tasks[i].text = updated.text
                        tasks[i].isChecked = updated.isChecked
                    }
                }
            }

            // logAll()

            callback()
        }
    }
}