package com.practicum.playlistmaker.search.ui.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.ui_utils.compose.Fonts

@Composable
fun TextNormal(text: String) {
    Text(
        text = text,
        fontFamily = Fonts.ysDisplayRegular,
        style = TextStyle(
            color = colorResource(R.color.text_color),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
}