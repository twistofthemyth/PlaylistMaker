package com.practicum.playlistmaker.player.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentTrackBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.player.ui.view_model.TrackViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TrackFragment : Fragment() {

    private val viewModel: TrackViewModel by viewModel {
        parametersOf(
            requireArguments().getString(
                TRACK_ID_LABEL
            )
        )
    }

    private val mediaViewModel: MediaViewModel by activityViewModel<MediaViewModel>()

    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TRACK_ID_LABEL = "track_id"
        fun createArgs(trackId: String): Bundle = bundleOf(TRACK_ID_LABEL to trackId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releasePlayer()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPlayer()
    }

    private fun setupFragment() {
        viewModel.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is TrackViewModel.ScreenState.Loading -> {}

                is TrackViewModel.ScreenState.Content -> {
                    binding.timeTv.text = it.position
                    binding.playTrackIv.setImageResource(it.iconResId)
                    binding.likeTrackIv.setImageResource(if (it.isFavorite) R.drawable.button_like_track_liked else R.drawable.button_like_track)
                    setupTrackInfo(it.track)
                }

                is TrackViewModel.ScreenState.Error -> {}
            }
        }
        binding.playTrackIv.setOnClickListener { viewModel.togglePlayer() }
        binding.likeTrackIv.setOnClickListener {
            viewModel.toggleTrackFavorites()
            lifecycleScope.async {
                delay(200)
                mediaViewModel.updateFavoritePlayList()
            }
        }
    }

    private fun setupToolbar() {
        binding.arrowBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
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