package com.example.studybuddy.utilities

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog

object CONF {
    var sharedPreferences: SharedPreferences? = null

    val languages = listOf(
        "English", "Tiếng Việt"
    )

    val language_codes = listOf(
        "en", "vi"
    )

    val fontsizes = listOf(
        "13dp", "16dp"
    )

    val datetime_formats = listOf(
        "dd/mm/yyyy", "mm/dd/yyyy"
    )

    val SWITCH_BUTTON_KEY = "switch"
    val LANGUAGE_KEY = "language"
    val PREF_KEY = "pref"
}

object PreferencesManager {
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize the shared preferences in the application context (this will be done once)
    fun initialize(context: Context) {
        sharedPreferences = context.applicationContext.getSharedPreferences(CONF.PREF_KEY, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun getInt(key: String, defValue: Int = -1): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun getString(key: String, defValue: String? = null): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
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