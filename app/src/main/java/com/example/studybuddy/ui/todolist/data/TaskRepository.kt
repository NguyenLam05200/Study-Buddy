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
        return realm.query<Task>().count().find() == 0L
    }

    // returned managed objects list
    suspend fun getAll(): List<Task> {
        return withContext(Dispatchers.IO)
        {
            val temp = realm.query<Task>().find()

            temp
        }
    }

    fun getAllFlow(): Flow<List<Task>> {
        return realm.query<Task>().asFlow().map { it.list }
    }

    // return a managed object
    suspend fun add(item: Task) : Task {
        return withContext(Dispatchers.IO)
        {
            realm.write {
                val managedItem = copyToRealm(item)

                managedItem
            }
        }
    }

    suspend fun update(uuid: String, updated: Task) {
        withContext(Dispatchers.IO) {
            realm.write {
                val item = realm.query<Task>("uuid = $0", uuid).find().firstOrNull()

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

    suspend fun remove(item: Task) {
        withContext(Dispatchers.IO)
        {
            realm.write {
                findLatest(item)?.let { delete(it) }
            }
        }
    }
}