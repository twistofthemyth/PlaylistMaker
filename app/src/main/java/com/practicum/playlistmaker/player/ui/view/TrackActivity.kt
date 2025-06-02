package com.practicum.playlistmaker.player.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.player.ui.view_model.TrackViewModel
import com.practicum.playlistmaker.search.domain.models.Track

class TrackActivity : AppCompatActivity() {

    private lateinit var viewModel: TrackViewModel
    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val track = Gson().fromJson(intent.getStringExtra("track"), Track::class.java)
        viewModel = TrackViewModel.getViewModelFactory(track, application)
            .create(TrackViewModel::class.java)

        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupPlayer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun setupPlayer() {
        viewModel.getNewScreenState().observe(this){
            binding.timeTv.text = it.position
            binding.playTrackIv.setImageResource(it.iconResId)
            setupTrackInfo(it.track)
        }
        binding.playTrackIv.setOnClickListener { viewModel.togglePlayer() }
    }

    private fun setupToolbar() {
        binding.arrowBackIv.setOnClickListener { finish() }
    }

    private fun setupTrackInfo(track: Track) {
            if (track.collectionName.isEmpty()) {
                binding.albumNameTv.visibility = View.GONE
                binding.albumNameValueTv.visibility = View.GONE
            } else {
                binding.albumNameValueTv.text = track.collectionName
            }

            binding.albumIv.apply {
                Glide.with(this.rootView)
                    .load(track.coverArtwork.toUri())
                    .placeholder(R.drawable.placeholder_album)
                    .centerInside()
                    .transform(RoundedCorners(resources.getInteger(R.integer.album_image_corner)))
                    .into(this)
            }

            binding.trackNameTv.text = track.trackName
            binding.authorNameTv.text = track.artistName
            binding.trackYearValueTv.text = track.releaseDate.replaceRange(
                track.releaseDate.indexOf("-"),
                track.releaseDate.length,
                ""
            )
            binding.trackGenreValueTv.text = track.primaryGenreName
            binding.trackCountryValueTv.text = track.country
            binding.trackDurationValueTv.text = track.trackTime
    }
}