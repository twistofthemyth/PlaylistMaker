package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.media.ui.view_model.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistsFragment : Fragment() {
    private val viewModel: MediaViewModel by activityViewModel<MediaViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private var _playlistAdapter: PlaylistAdapter? = null
    private val playlistAdapter get() = _playlistAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState == null) {
            _binding = FragmentPlaylistsBinding.inflate(layoutInflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _playlistAdapter = PlaylistAdapter {}

        viewModel.getPlaylistState().observe(viewLifecycleOwner){
            when(it) {
                is MediaViewModel.PlaylistState.Empty -> {
                    binding.errorIv.isVisible = true
                    binding.errorTv.isVisible = true
                    binding.actionBtn.isVisible = true
                    binding.playlistsRv.isVisible = false
                    binding.playlistsRvSpacer.isVisible = false
                }

                is MediaViewModel.PlaylistState.Content -> {
                    binding.errorIv.isVisible = false
                    binding.errorTv.isVisible = false
                    binding.actionBtn.isVisible = true
                    binding.playlistsRv.isVisible = true
                    binding.playlistsRvSpacer.isVisible = true

                    binding.playlistsRv.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.playlistsRv.adapter = playlistAdapter
                    playlistAdapter.updateList(it.playlists)
                }
            }
        }

        binding.actionBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _playlistAdapter = null
    }
}