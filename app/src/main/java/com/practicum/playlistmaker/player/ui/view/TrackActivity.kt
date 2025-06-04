package com.practicum.playlistmaker.player.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.player.ui.view_model.TrackViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class TrackActivity : AppCompatActivity(), KoinComponent {

    private val viewModel: TrackViewModel by viewModel()
    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                binding.albumNameTv.isVisible = false
                binding.albumNameValueTv.isVisible = false
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