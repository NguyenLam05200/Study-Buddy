package com.example.studybuddy.ui.todolist.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class TaskModel : RealmObject {
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
    var text: String = "New task"
    var isChecked: Boolean = false

    constructor() {}

    constructor(text: String = "New task", isChecked: Boolean = false) {
        this.text = text
        this.isChecked = isChecked
    }
}