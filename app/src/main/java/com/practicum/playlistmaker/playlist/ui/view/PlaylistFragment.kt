package com.practicum.playlistmaker.playlist.ui.view

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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view.TrackListAdapter
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import com.practicum.playlistmaker.util.ui_utils.TrackNavigatableViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private val args by navArgs<PlaylistFragmentArgs>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel by viewModel<PlaylistViewModel> { parametersOf(args.playlistId) }

    private var _trackListAdapter: TrackListAdapter? = null
    private val trackListAdapter get() = _trackListAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTrackList()
        observeNavigation()
        observeViewState()
        setupBottomSheet()
        setupToolbar()
    }

    fun setupTrackList() {
        _trackListAdapter = TrackListAdapter(
            onTrackClicked = { track -> playlistViewModel.clickTrack(track) },
            onTrackLongClicked = { track -> showDeleteTrackAlert(track) }
        )
        binding.tacksRv.apply {
            adapter = _trackListAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun debounceOptionButton() {
        lifecycleScope.async {
            binding.optionIv.isClickable = false
            delay(200)
            binding.optionIv.isClickable = true
        }
    }

    private fun observeNavigation() {
        playlistViewModel.getNavigationEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { destination ->
                when (destination) {
                    is TrackNavigatableViewModel.NavigationDestination.ToTrack -> {
                        val direction =
                            PlaylistFragmentDirections.actionPlaylistFragmentToTrackFragment(
                                destination.track.trackId
                            )
                        findNavController().navigate(direction)
                    }
                }
            })
    }

    private fun observeViewState() {
        playlistViewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistViewModel.PlaylistState.Content -> {
                    binding.playlistIv.isVisible = true
                    binding.nameTv.isVisible = true
                    binding.yearTv.isVisible = true
                    binding.durationTv.isVisible = true
                    binding.delimiterIv.isVisible = true
                    binding.trackCountTv.isVisible = true
                    binding.shareIv.isVisible = true
                    binding.optionIv.isVisible = true
                    binding.progressBar.isVisible = false

                    Glide.with(binding.playlistIv)
                        .load(it.playlist.image.toUri())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_album)
                        .into(binding.playlistIv)

                    binding.nameTv.text = it.playlist.name
                    binding.yearTv.text = it.playlist.description
                    binding.durationTv.text = it.duration
                    binding.trackCountTv.text = it.count

                    binding.optionIv.setOnClickListener {
                        debounceOptionButton()
                        BottomSheetPlaylistOptionsFragment(args.playlistId).show(
                            parentFragmentManager,
                            BottomSheetPlaylistOptionsFragment.TAG
                        )
                    }

                    if (it.playlist.track.isEmpty()) {
                        binding.errorTv.isVisible = true
                    } else {
                        binding.errorTv.isVisible = false
                        trackListAdapter.updateList(it.playlist.track)
                    }
                }

                is PlaylistViewModel.PlaylistState.Loading -> {
                    binding.playlistIv.isVisible = false
                    binding.nameTv.isVisible = false
                    binding.yearTv.isVisible = false
                    binding.durationTv.isVisible = false
                    binding.delimiterIv.isVisible = false
                    binding.trackCountTv.isVisible = false
                    binding.shareIv.isVisible = false
                    binding.optionIv.isVisible = false
                    binding.progressBar.isVisible = true
                    binding.errorTv.isVisible = false
                }
            }
        }
    }

    private fun setupBottomSheet() {
        val behavior = BottomSheetBehavior.from<LinearLayout>(binding.standardBottomSheet)
        val initState = behavior.state

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.state = initState
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun setupToolbar() {
        binding.arrowBackIv.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    fun showDeleteTrackAlert(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_delete_track_title)
            .setMessage(R.string.dialog_delete_track_message)
            .setNegativeButton(R.string.dialog_no) { dialog, which -> }
            .setPositiveButton(R.string.dialog_yes) { dialog, which ->
                playlistViewModel.deleteTrackFromPlaylist(track)
                showDeleteToast()
            }
            .show()
    }

    fun showDeleteToast() {
        Toast.makeText(
            requireContext(),
            R.string.track_deleted,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _trackListAdapter = null
    }
}