package com.example.studybuddy

import android.app.Application
import com.example.studybuddy.services.NotificationService

class StudyBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Khởi tạo NotificationService
        NotificationService.init(this)
    }
}
