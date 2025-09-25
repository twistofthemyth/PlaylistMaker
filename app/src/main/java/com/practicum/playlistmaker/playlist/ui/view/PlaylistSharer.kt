package com.practicum.playlistmaker.playlist.ui.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.util.ui_utils.IntentUtils

interface PlaylistSharer {
    fun makeShareIntent(event: PlaylistViewModel.ShareState.Content): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            setData(IntentUtils.URI_TYPE_SMS.toUri())
            putExtra(IntentUtils.EXTRA_TYPE_SMS_TEXT, event.text)
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