package com.practicum.playlistmaker.util.ui_utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

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

@Composable
fun ProgressBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = colorResource(R.color.primary_color),
            trackColor = colorResource(R.color.background)
        )
    }
}

@Composable
fun SearchEditText(onValueChange: (String) -> Unit, onValueClean: () -> Unit) {
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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

@Composable
fun TrackItems(tracks: List<Track>, onClick: (Track) -> Unit) {
    LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
        items(tracks) {
            TrackItem(track = it, onClick = onClick)
        }
    }
}

@Composable
fun TrackItem(track: Track, onClick: (Track) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(
                onClick = { onClick(track) }
            ),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        AsyncImage(
            modifier = Modifier.size(45.dp),
            model = track.coverArtwork,
            contentDescription = null,
            placeholder = painterResource(R.drawable.placeholder_album)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            TextNormal(track.trackName)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextSmall(track.artistName)
                Icon(
                    painter = painterResource(R.drawable.ic_dot_delimiter),
                    contentDescription = null,
                    tint = colorResource(R.color.et_hint_color)
                )
                TextSmall(track.trackTime)
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            tint = colorResource(R.color.et_hint_color)
        )
    }
}

@Composable
fun InfoMessage(
    textId: Int,
    descriptionId: Int? = null,
    imageId: Int? = null,
    buttonTextId: Int? = null,
    onButtonClickAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageId != null) {
            Image(
                modifier = Modifier.padding(bottom = 16.dp, top = 102.dp),
                painter = painterResource(R.drawable.placeholder_net_error),
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

@Composable
fun TextScreenTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 16.dp),
        style = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Fonts.ysDisplayMedium
        ),
        color = colorResource(R.color.text_color)
    )
}

@Composable
fun TextMessage(text: String) {
    Text(
        text = text,
        fontFamily = Fonts.ysDisplayMedium,
        style = TextStyle(
            color = colorResource(R.color.text_color),
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

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

@Composable
fun TextSmall(text: String) {
    Text(
        text = text,
        fontFamily = Fonts.ysDisplayRegular,
        style = TextStyle(
            color = colorResource(R.color.text_subtitle_color),
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp
        )
    )
}