package com.example.studybuddy.ui.todolist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.ui.todolist.data.TaskModel
import com.example.studybuddy.ui.todolist.data.TaskRepository
import io.realm.kotlin.ext.isManaged
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RealmLiveData<T : RealmObject>(private val repository: TaskRepository, private val scope: CoroutineScope) :
    LiveData<List<T>>() {
    private var job: Job? = null

    override fun onActive() {
        job = scope.launch {
            repository.getAll_Flow().collect { tasks ->
                postValue(tasks as List<T>)
            }
        }
    }

    override fun onInactive() {
        job?.cancel()
    }
}

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    lateinit var tasks: LiveData<List<TaskModel>>

    fun initialize() {
        viewModelScope.launch {
            tasks = RealmLiveData(repository, viewModelScope)
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

    fun getTask(pos: Int): TaskModel? {
        return tasks.value?.get(pos)
    }

    fun getSize(): Int {
        return tasks.value?.size ?: 0
    }

    fun add(item: TaskModel) {
        viewModelScope.launch {
            val managedItem = repository.add(item)
            Log.d("TODOLIST", "Adding in ViewModel: ${item.uuid}, ${item.text}, ${item.isChecked}")
            Log.d("TODOLIST", "Is Managed: ${managedItem.isManaged()}")

            logAll()
        }
    }

    fun remove(item: TaskModel) {
        viewModelScope.launch {
            repository.remove(item)

            logAll()
        }
    }

    fun update(pos: Int, uuid: String, updated: TaskModel) {
        viewModelScope.launch {
            repository.update(uuid, updated)

            logAll()
        }
    }
}