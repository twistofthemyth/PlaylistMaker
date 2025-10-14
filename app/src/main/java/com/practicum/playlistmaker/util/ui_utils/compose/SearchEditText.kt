package com.practicum.playlistmaker.util.ui_utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R

@Composable
fun SearchEditText(onValueChange: (String) -> Unit, onValueClean: () -> Unit) {
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(
                color = colorResource(R.color.et_color),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 13.dp)
                .clickable(onClick = {
                    searchText = ""
                    onValueClean.invoke()
                })
        ) {
            Icon(
                modifier = Modifier.width(16.dp),
                painter = painterResource(R.drawable.ic_search),
                tint = colorResource(R.color.et_hint_color),
                contentDescription = null
            )
        }

        BasicTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 36.dp, end = 40.dp, top = 9.dp, bottom = 8.dp),
            singleLine = true,
            cursorBrush = SolidColor(colorResource(R.color.et_hint_color)),
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    Text(
                        text = "Поиск",
                        color = colorResource(R.color.et_hint_color),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Fonts.ysDisplayRegular
                        ),
                    )
                }
                innerTextField()
            }
        )

        if (searchText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .clickable(onClick = {
                        searchText = ""
                        onValueClean.invoke()
                    })
            ) {
                Icon(
                    modifier = Modifier.width(16.dp),
                    painter = painterResource(R.drawable.ic_clear),
                    tint = colorResource(R.color.et_hint_color),
                    contentDescription = null
                )
            }
        }
    }
}