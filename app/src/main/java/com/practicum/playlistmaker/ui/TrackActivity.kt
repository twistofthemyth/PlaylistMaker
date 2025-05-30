package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.PlayerHolder

class TrackActivity : AppCompatActivity() {

    lateinit var playerHolder: PlayerHolder
    lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        setupToolbar()
        setupTrackInfo()
        setupPlayer()
    }

    override fun onPause() {
        super.onPause()
        playerHolder.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerHolder.release()
    }

    private fun setupToolbar() {
        findViewById<ImageView>(R.id.arrow_back_iv).setOnClickListener { finish() }
    }

    private fun setupTrackInfo() {
        track = Gson().fromJson(intent.getStringExtra("track"), Track::class.java)
        val albumNameTitle = findViewById<TextView>(R.id.album_name_tv)

        findViewById<TextView>(R.id.album_name_value_tv).apply {
            if (track.collectionName.isEmpty()) {
                visibility = View.GONE
                albumNameTitle.visibility = View.GONE
            } else {
                text = track.collectionName
            }
        }

        findViewById<ImageView>(R.id.album_iv).apply {
            Glide.with(this.rootView)
                .load(track.coverArtwork.toUri())
                .placeholder(R.drawable.placeholder_album)
                .centerInside()
                .transform(RoundedCorners(resources.getInteger(R.integer.album_image_corner)))
                .into(this)
        }

        findViewById<TextView>(R.id.track_name_tv).apply {
            text = track.trackName
        }

        findViewById<TextView>(R.id.author_name_tv).apply {
            text = track.artistName
        }

        findViewById<TextView>(R.id.track_year_value_tv).apply {
            text = track.releaseDate.replaceRange(
                track.releaseDate.indexOf("-"),
                track.releaseDate.length,
                ""
            )
        }

        findViewById<TextView>(R.id.track_genre_value_tv).apply {
            text = track.primaryGenreName
        }

        findViewById<TextView>(R.id.track_country_value_tv).apply {
            text = track.country
        }

        findViewById<TextView>(R.id.track_duration_value_tv).apply {
            text = track.trackTime
        }
    }

    private fun setupPlayer() {
        val playPosition = findViewById<TextView>(R.id.time_tv)
        val playIv = findViewById<ImageView>(R.id.play_track_iv).apply { isEnabled = false }

        playerHolder = PlayerHolder(
            url = track.previewUrl,
            onPositionUpdateAction = { playPosition.text = it },
            onPlayAction = { playIv.setImageResource(R.drawable.button_pause_track) },
            onPauseAction = { playIv.setImageResource(R.drawable.button_play_track) },
            onCompleteAction = { playIv.setImageResource(R.drawable.button_play_track) },
            onPrepareAction = { playIv.isEnabled = true }
        ).apply {
            preparePlayer()
        }

        playIv.setOnClickListener {
            playerHolder.playbackControl()
        }
    }
}