package com.example.studybuddy.data

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object CONF {
    val languages = listOf(
        "English",
        "Tiếng Việt"
    )

    val fontsizes = listOf(
        "13dp",
        "16dp"
    )

    val datetime_formats = listOf(
        "dd/mm/yyyy",
        "mm/dd/yyyy"
    )
}

fun showInfoDialog(context: Context, title: String = "Default", message: String, ok_button_text: String = "OK") {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton(ok_button_text) { dialog: DialogInterface, which: Int ->
        dialog.dismiss()
    }

    // Optional: Set a negative button, like a cancel button
//        builder.setNegativeButton("Cancel") { dialog, which ->
//            dialog.dismiss()  // Close the dialog
//        }

    val dialog = builder.create()
    dialog.show()
}