package com.example.studybuddy.ui.filemanager.data

import java.util.UUID

class FileItem {
    var uuid: String = UUID.randomUUID().toString()
    var uri: String = ""
    var displayName: String = ""

    constructor() {}

    constructor(uri: String, displayName: String) {
        this.uri = uri
        this.displayName = displayName
    }
}