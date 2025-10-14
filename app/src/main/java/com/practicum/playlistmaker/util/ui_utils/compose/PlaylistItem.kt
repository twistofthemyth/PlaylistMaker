package com.practicum.playlistmaker.util.ui_utils.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.ui.compose.TextSmall

@Composable
fun PlaylistItem(playlist: Playlist, onClickPlaylist: (Playlist) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { onClickPlaylist.invoke(playlist) })
    ) {
        if (playlist.image == null) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(R.drawable.placeholder_album),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                model = playlist.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        TextSmall(playlist.name)
        Spacer(modifier = Modifier.size(4.dp))
        TextSmall(
            pluralStringResource(
                R.plurals.plular_track,
                playlist.track.size,
                playlist.track.size
            )
        )
    }
}