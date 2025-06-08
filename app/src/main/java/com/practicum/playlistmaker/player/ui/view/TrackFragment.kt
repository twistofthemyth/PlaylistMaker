package com.practicum.playlistmaker.player.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.player.ui.view_model.TrackViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class TrackFragment : Fragment() {

    private val viewModel: TrackViewModel by activityViewModel<TrackViewModel>()

    private var _binding: ActivityTrackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityTrackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releasePlayer()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun setupPlayer() {
        viewModel.getNewScreenState().observe(viewLifecycleOwner){
            binding.timeTv.text = it.position
            binding.playTrackIv.setImageResource(it.iconResId)
            setupTrackInfo(it.track)
        }
        binding.playTrackIv.setOnClickListener { viewModel.togglePlayer() }
    }

    private fun setupToolbar() {
        binding.arrowBackIv.setOnClickListener { parentFragmentManager.popBackStack() }
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