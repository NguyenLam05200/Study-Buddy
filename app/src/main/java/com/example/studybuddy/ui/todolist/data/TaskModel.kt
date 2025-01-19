package com.example.studybuddy.ui.todolist.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studybuddy.ui.todolist.TaskViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class Task : RealmObject {
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
    var text: String = "Default task string"
    var isChecked: Boolean = false

    constructor() {}

    constructor(text: String = "Default task string", isChecked: Boolean = false) {
        this.text = text
        this.isChecked = isChecked
    }
}