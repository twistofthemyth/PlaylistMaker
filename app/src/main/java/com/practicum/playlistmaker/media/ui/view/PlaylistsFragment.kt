package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistsFragment : Fragment() {
    private val viewModel: MediaViewModel by activityViewModel<MediaViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

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

        viewModel.getPlaylistState().observe(viewLifecycleOwner){
            when(it) {
                is MediaViewModel.PlaylistState.Empty -> {
                    binding.errorIv.isVisible = true
                    binding.errorTv.isVisible = true
                    binding.actionBtn.isVisible = true
                }
            }
        }
    }
}