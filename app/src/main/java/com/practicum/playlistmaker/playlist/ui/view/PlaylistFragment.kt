package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private val args by navArgs<PlaylistFragmentArgs>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistViewModel by viewModel<PlaylistViewModel> { parametersOf(args.playlistId) }

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
                        .placeholder(R.drawable.placeholder_album)
                        .into(binding.playlistIv)

                    binding.nameTv.text = it.playlist.name
                    binding.yearTv.text = it.playlist.description
                    binding.durationTv.text = it.duration
                    binding.trackCountTv.text = it.count
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
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}