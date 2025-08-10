package com.practicum.playlistmaker.playlist.ui.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel

interface PlaylistSharer {

    fun makeShareIntent(event: PlaylistViewModel.ShareState.Content): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            setData("smsto:".toUri())
            putExtra("sms_body", event.text)
        }
    }

    fun makeEmptyListToast(context: Context): Toast {
        return Toast.makeText(
            context,
            R.string.no_tracks_to_share,
            Toast.LENGTH_SHORT
        )
    }
}