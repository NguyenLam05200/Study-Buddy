package com.example.studybuddy.ui.todolist.data

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isManaged
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TaskRepository(private val realm: Realm) {
    suspend fun isEmpty(): Boolean {
        return realm.query<TaskModel>().count().find() == 0L
    }

    // returned managed objects list
    suspend fun getAll(): List<TaskModel> {
        return withContext(Dispatchers.IO)
        {
            val temp = realm.query<TaskModel>().find()

            temp
        }
    }

    suspend fun getAll_Flow(): Flow<List<TaskModel>> {
        return withContext(Dispatchers.IO){
            realm.query<TaskModel>().asFlow().map { it.list }
        }
    }

    // return a managed object
    suspend fun add(item: TaskModel) : TaskModel {
        return withContext(Dispatchers.IO)
        {
            realm.write {
                val managedItem = copyToRealm(item)

                managedItem
            }
        }
    }

    suspend fun update(uuid: String, updated: TaskModel) {
        withContext(Dispatchers.IO) {
            realm.write {
                val item = realm.query<TaskModel>("uuid = $0", uuid).find().firstOrNull()

                val liveItem = item?.let { findLatest(item) }

                liveItem?.let {
                    it.text = updated.text
                    it.isChecked = updated.isChecked
                    Log.d("TODOLIST", "Updating in Repository: ${it.uuid}, ${it.text}, ${it.isChecked}")
                    Log.d("TODOLIST", "Is Managed: ${it.isManaged()}")
                }
            }
        }
    }

    suspend fun remove(item: TaskModel) {
        withContext(Dispatchers.IO)
        {
            realm.write {
                findLatest(item)?.let { delete(it) }
            }
        }
    }
}