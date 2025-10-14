package com.practicum.playlistmaker.util.ui_utils.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R

@Composable
fun InfoMessageButton(textId: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 18.dp, bottom = 12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.text_color),
                contentColor = colorResource(R.color.background)
            ),
            shape = RoundedCornerShape(56.dp)
        ) {
            Text(text = stringResource(textId))
        }
    }
}