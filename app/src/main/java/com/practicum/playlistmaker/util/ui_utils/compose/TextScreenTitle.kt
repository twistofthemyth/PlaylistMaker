package com.practicum.playlistmaker.search.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.ui_utils.compose.Fonts

@Composable
fun TextScreenTitle(textId: Int) {
    Text(
        text = stringResource(textId),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 16.dp, start = 16.dp),
        style = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Fonts.ysDisplayMedium
        ),
        color = colorResource(R.color.text_color)
    )
}