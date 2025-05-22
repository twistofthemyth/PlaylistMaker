package com.practicum.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefInputStorage(private val sharedPreferences: SharedPreferences) {

    fun loadStringValue(key: String): String {
        return sharedPreferences.getString(key, "").toString();
    }

    fun saveStringValue(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }
}