package com.example.studybuddy.data.repository

import android.util.Log
import com.example.studybuddy.data.local.model.Task
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isManaged
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
class TaskRepository(private val realm: Realm) {

    // Lấy tất cả task
    suspend fun getAllTasks(): List<Task> {
        return realm.query<Task>().find()
    }

    // Thêm task mới
    suspend fun addTask(task: Task) {
        realm.write {
            copyToRealm(task)
        }
    }

    // Cập nhật task
    suspend fun updateTask(task: Task) {
        realm.write {
            val managedTask = query<Task>("uuid == $0", task.uuid).first().find()
            managedTask?.apply {
                text = task.text
                isChecked = task.isChecked
            }
        }
    }

    // Xóa task
    suspend fun deleteTask(task: Task) {
        realm.write {
            val managedTask = findLatest(task)
            if (managedTask != null) delete(managedTask)
        }
    }

    // Kiểm tra xem danh sách task có trống không
    suspend fun isEmpty(): Boolean {
        return realm.query<Task>().count().find() == 0L
    }
}
