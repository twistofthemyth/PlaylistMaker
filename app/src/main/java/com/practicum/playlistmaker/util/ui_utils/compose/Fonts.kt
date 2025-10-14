package com.practicum.playlistmaker.util.ui_utils.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.practicum.playlistmaker.R

object Fonts {
    val ysDisplayRegular: FontFamily
        @Composable get() = FontFamily(
            Font(R.font.ys_display_regular, FontWeight.Normal)
        )

    val ysDisplayMedium: FontFamily
        @Composable get() = FontFamily(
            Font(R.font.ys_display_medium, FontWeight.Normal)
        )
}