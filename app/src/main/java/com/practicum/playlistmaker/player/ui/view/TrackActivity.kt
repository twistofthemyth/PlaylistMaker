package com.practicum.playlistmaker.player.ui.view

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
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.TrackViewModel
import com.practicum.playlistmaker.search.domain.models.Track

class TrackActivity : AppCompatActivity() {

    private lateinit var viewModel: TrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        setupViewModel()
        setupToolbar()
        setUpInput()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun setupViewModel() {
        val track = Gson().fromJson(intent.getStringExtra("track"), Track::class.java)
        viewModel = TrackViewModel.getViewModelFactory(track).create(TrackViewModel::class.java)
        TrackViewModel(track, this.application)
        viewModel.getScreenState().observe(this) {
            when (it) {
                is TrackViewModel.TrackScreenState.Content -> {
                    setupTrackInfo(it.track)
                }
            }
        }

        val playIv = findViewById<ImageView>(R.id.play_track_iv)
        val playPosition = findViewById<TextView>(R.id.time_tv)

        viewModel.getPlayStatus().observe(this) {
            playPosition.text = it.position
            when (it.playerState) {
                PlayerState.STATE_PLAYING -> playIv.setImageResource(R.drawable.button_pause_track)
                PlayerState.STATE_DEFAULT,
                PlayerState.STATE_PREPARED,
                PlayerState.STATE_PAUSED -> playIv.setImageResource(R.drawable.button_play_track)
            }
        }
    }

    private fun setUpInput() {
        findViewById<ImageView>(R.id.play_track_iv).setOnClickListener { viewModel.togglePlayer() }
    }

    private fun setupToolbar() {
        findViewById<ImageView>(R.id.arrow_back_iv).setOnClickListener { finish() }
    }

    private fun setupTrackInfo(track: Track) {
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
}