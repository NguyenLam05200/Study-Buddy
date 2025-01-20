package com.example.studybuddy.data.repository

import android.util.Log
import com.example.studybuddy.ui.todolist.data.TaskModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isManaged
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
class TaskRepository(private val realm: Realm) {

    // Lấy tất cả task
    suspend fun getAllTasks(): List<TaskModel> {
        return realm.query<TaskModel>().find()
    }

    // Thêm task mới
    suspend fun addTask(task: TaskModel) {
        realm.write {
            copyToRealm(task)
        }
    }

    // Cập nhật task
    suspend fun updateTask(uuid: String, task: TaskModel) {
        realm.write {
            val managedTask = query<TaskModel>("uuid == $0", uuid).first().find()
            managedTask?.apply {
                text = task.text
                isChecked = task.isChecked
            }
        }
    }

    // Xóa task
    suspend fun deleteTask(task: TaskModel) {
        realm.write {
            val managedTask = findLatest(task)
            if (managedTask != null) delete(managedTask)
        }
    }

    // Kiểm tra xem danh sách task có trống không
    suspend fun isEmpty(): Boolean {
        return realm.query<TaskModel>().count().find() == 0L
    }
}

