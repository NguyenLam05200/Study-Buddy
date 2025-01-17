package com.example.studybuddy.ui.course

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.local.model.CourseModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmInstant

class ManageCourseViewModel : ViewModel() {

    private val realm = DatabaseProvider.getDatabase() // Khởi tạo database

    // Lấy khóa học theo ID
    fun getCourseById(courseId: Long): LiveData<CourseModel?> {
        val liveData = MutableLiveData<CourseModel?>()
        val course = realm.query<CourseModel>("id == $0", courseId).first().find()
        liveData.postValue(course)
        return liveData
    }

    // Thêm mới khóa học
    fun addCourse(course: CourseModel) {
        realm.writeBlocking {
            copyToRealm(course)
        }
    }

    // Cập nhật khóa học
    fun updateCourse(course: CourseModel) {
        realm.writeBlocking {
            val existingCourse = query<CourseModel>("id == $0", course.id).first().find()
            if (existingCourse != null) {
                Log.d(
                    "ManageCourseViewModel",
                    "Before Update - startTime: ${existingCourse.startTime}"
                )
                Log.d("ManageCourseViewModel", "Before Update - endTime: ${existingCourse.endTime}")
                Log.d(
                    "ManageCourseViewModel",
                    "Before Update - startDate: ${existingCourse.startDate}"
                )
                Log.d("ManageCourseViewModel", "Before Update - endDate: ${existingCourse.endDate}")

                existingCourse.name = course.name
                existingCourse.dayOfWeek = course.dayOfWeek
                existingCourse.startTime = course.startTime
                existingCourse.endTime = course.endTime
                existingCourse.startDate = course.startDate
                existingCourse.endDate = course.endDate
                existingCourse.hasReminder = course.hasReminder
                existingCourse.room = course.room

                Log.d(
                    "ManageCourseViewModel",
                    "After Update - startTime: ${existingCourse.startTime}"
                )
                Log.d("ManageCourseViewModel", "After Update - endTime: ${existingCourse.endTime}")
            } else {
                Log.e("ManageCourseViewModel", "Course with id ${course.id} not found!")
            }
        }
    }
}
