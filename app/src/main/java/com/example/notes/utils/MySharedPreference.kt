package com.example.notes.utils

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {
    private const val PREF_NAME = "my_shared_prefs"
    private const val PREF_MODE = Context.MODE_PRIVATE

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, PREF_MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var token: String
        get() = prefs.getString("token", "null")!!
        set(value) = prefs.edit {
            if(value!=null) {
                it.putString("token", value)
            }
        }

}