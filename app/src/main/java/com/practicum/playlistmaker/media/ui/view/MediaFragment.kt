package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentComposeBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MediaFragment : Fragment() {

    private val viewModel: MediaViewModel by activityViewModel<MediaViewModel>()
    private var _binding: FragmentComposeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComposeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            MediaScreen(
                viewModel = viewModel,
                onClickTrack = {
                    viewModel.clickTrack(it)
                },
                onCreatePlaylist = { findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment) },
                onClickPlaylist = {
                    val direction =
                        MediaFragmentDirections.actionMediaFragmentToPlaylistFragment(it.id)
                    findNavController().navigate(direction)
                })
        }
        viewModel.getTrackNavigationEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { track ->
                val direction =
                    MediaFragmentDirections.actionMediaFragmentToTrackFragment(track.trackId)
                findNavController().navigate(direction)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}