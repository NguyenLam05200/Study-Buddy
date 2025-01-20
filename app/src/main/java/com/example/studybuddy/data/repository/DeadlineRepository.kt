package com.example.studybuddy.data.repository

import android.util.Log
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.CourseModel
import com.example.studybuddy.data.local.model.DeadlineModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeadlineRepository(private val realm: Realm) {

    fun getAllDeadlines(): Flow<List<DeadlineModel>> {
        return realm.query<DeadlineModel>().asFlow().map { it.list }
    }

    suspend fun isEmpty(): Boolean {
        return realm.query<DeadlineModel>().count().find() == 0L
    }

    suspend fun getDeadlineById(DeadlineId: Int): DeadlineModel? {
        return realm.query<DeadlineModel>("id == $0", DeadlineId).first().find()
    }


    suspend fun addDeadlines(deadlines: List<DeadlineModel>) {
        realm.write {
            var nextId = DatabaseProvider.generateDeadlineAutoIncrementId() // Lấy ID bắt đầu

            deadlines.forEach { deadline ->
                deadline.id = nextId // Gán ID tự động
                deadline.updateDueDate() // Tính toán lại `dueDate`
                copyToRealm(deadline)
                nextId++ // Tăng ID cho lần tiếp theo
            }
        }
    }

    suspend fun addDeadline(deadline: DeadlineModel) {
        realm.write {
            deadline.updateDueDate() // Tính toán lại `dueDate`
            deadline.id = DatabaseProvider.generateDeadlineAutoIncrementId() // Tạo ID tự động
            copyToRealm(deadline)
        }
    }


    suspend fun deleteDeadline(deadline: DeadlineModel) {
        realm.write {
            findLatest(deadline)?.let { delete(it) }
        }
    }

    suspend fun updateDeadline(deadline: DeadlineModel) {
        try {
            realm.write {
                // Query the managed object from Realm
                val managedDeadline = query<DeadlineModel>("id == $0", deadline.id).first().find()
                if (managedDeadline != null) {
                    managedDeadline.apply {
                        name = deadline.name
                        time = deadline.time
                        courseId = deadline.courseId
                        dueDate = deadline.dueDate
                        updateDueDate()
                    }
                } else {
                    Log.e("____Repo", "updateDeadline failed: Deadline not found with id ${deadline.id}")
                }
            }
        } catch (e: Exception) {
            Log.e("____RepoError", "updateDeadline failed: ${e.message}", e)
            throw e
        }
    }

    fun logAllDeadlines() {
        val Deadlines = realm.query<DeadlineModel>().find()
        Deadlines.forEach {
            Log.d("____Repo", "Deadline: ${it.name}, time: ${it.time}, currentTime: ${System.currentTimeMillis()}")
        }
    }

    suspend fun getUpcomingDeadlines(limit: Int): List<DeadlineModel> {
        val Deadlines = realm.query<DeadlineModel>().find()

        return Deadlines
    }

    fun getAllCourses(): List<CourseModel> {
        return realm.query<CourseModel>().find()
    }

}

