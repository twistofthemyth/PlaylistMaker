package com.practicum.playlistmaker.search.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

@Composable
fun TrackItems(modifier: Modifier = Modifier, tracks: List<Track>, onClick: (Track) -> Unit) {
    LazyColumn(modifier = modifier) {
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
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
            model = track.coverArtwork,
            contentDescription = null,
            contentScale = ContentScale.Crop,
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
                TextTiny(track.artistName)
                Icon(
                    painter = painterResource(R.drawable.ic_dot_delimiter),
                    contentDescription = null,
                    tint = colorResource(R.color.list_item_icon_color)
                )
                TextTiny(track.trackTime)
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            tint = colorResource(R.color.list_item_icon_color)
        )
    }
}