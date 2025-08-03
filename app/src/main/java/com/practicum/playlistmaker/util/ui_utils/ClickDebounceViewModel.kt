package com.practicum.playlistmaker.util.ui_utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class ClickDebounceViewModel : ViewModel() {
    private var isClickActionAllowed = true

    protected fun isClickActionAllowed(): Boolean {
        val current = isClickActionAllowed
        if (isClickActionAllowed) {
            isClickActionAllowed = false
            viewModelScope.launch {
                delay(TOUCH_DEBOUNCE_IN_MILLIS)
                isClickActionAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val TOUCH_DEBOUNCE_IN_MILLIS = 1000L
    }
}