package com.practicum.playlistmaker.player.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentTrackBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.media.ui.view_model.PlaylistAdapter
import com.practicum.playlistmaker.player.ui.view_model.TrackViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TrackFragment : Fragment() {

    private val args by navArgs<TrackFragmentArgs>()
    private val viewModel: TrackViewModel by viewModel { parametersOf(args.trackId) }
    private val mediaViewModel: MediaViewModel by activityViewModel<MediaViewModel>()
    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!
    private var _playlistAdapter: PlaylistAdapter? = null
    private val playlistAdapter get() = _playlistAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _playlistAdapter = PlaylistAdapter {
            lifecycleScope.launch {
                val isAdded = viewModel.addTrackToPlaylist(it)
                if (isAdded) {
                    hideBottomSheet()
                    Toast.makeText(
                        requireContext(),
                        requireActivity().getString(R.string.track_added_to_playlist)
                            .format(it.name), Toast.LENGTH_SHORT
                    ).show()
                    mediaViewModel.updatePlaylist()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireActivity().getString(R.string.track_already_in_playlist)
                            .format(it.name), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        setupToolbar()
        setupFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releasePlayer()
        _binding = null
        _playlistAdapter = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPlayer()
    }

    private fun setupFragment() {
        observeTrackState()
        observePlaylistsState()

        binding.playlistsRv.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        binding.playTrackIv.setOnClickListener { viewModel.togglePlayer() }
        binding.likeTrackIv.setOnClickListener {
            viewModel.toggleTrackFavorites()
            lifecycleScope.async {
                delay(200)
                mediaViewModel.updateFavoritePlayList()
            }
        }

        hideBottomSheet()
        binding.addTrackIv.setOnClickListener { showBottomSheet() }

        binding.actionBtn.setOnClickListener {
            val direction =
                TrackFragmentDirections.actionTrackFragmentToCreatePlaylistFragment(trackId = args.trackId)
            findNavController().navigate(direction)
        }
    }

    private fun observeTrackState() {
        viewModel.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is TrackViewModel.TrackState.Loading -> {}

                is TrackViewModel.TrackState.Content -> {
                    binding.timeTv.text = it.position
                    binding.playTrackIv.setImageResource(it.iconResId)
                    binding.likeTrackIv.setImageResource(if (it.isFavorite) R.drawable.button_like_track_liked else R.drawable.button_like_track)
                    setupTrackInfo(it.track)
                }

                is TrackViewModel.TrackState.Error -> {}
            }
        }
    }

    private fun observePlaylistsState() {
        viewModel.getPlaylistsState().observe(viewLifecycleOwner) {
            when (it) {
                is TrackViewModel.PlaylistsState.Content -> {
                    playlistAdapter.updateList(it.playlists)
                }

                TrackViewModel.PlaylistsState.Loading -> {
                    playlistAdapter.updateList(emptyList())
                }
            }
        }
    }

    private fun hideBottomSheet() {
        val behavior = BottomSheetBehavior.from<LinearLayout>(binding.standardBottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showBottomSheet() {
        val behavior = BottomSheetBehavior.from<LinearLayout>(binding.standardBottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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