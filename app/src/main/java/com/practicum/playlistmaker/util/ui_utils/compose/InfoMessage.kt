package com.practicum.playlistmaker.util.ui_utils.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.search.ui.compose.TextMessage

@Composable
fun InfoMessage(
    modifier: Modifier = Modifier,
    textId: Int,
    descriptionId: Int? = null,
    imageId: Int? = null,
    buttonTextId: Int? = null,
    onButtonClickAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageId != null) {
            Image(
                modifier = Modifier.padding(bottom = 16.dp),
                painter = painterResource(imageId),
                contentDescription = null
            )
        }
        TextMessage(stringResource(textId))

        if (descriptionId != null) {
            Spacer(modifier = Modifier.padding(top = 16.dp))
            TextMessage(stringResource(descriptionId))
        }
        if (buttonTextId != null && onButtonClickAction != null) {
            InfoMessageButton(buttonTextId, onButtonClickAction)
        }
    }
}