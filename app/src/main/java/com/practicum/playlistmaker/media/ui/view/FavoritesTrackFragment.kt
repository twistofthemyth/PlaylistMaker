package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoritesTrackFragment : Fragment() {
    private val viewModel: MediaViewModel by activityViewModel<MediaViewModel>()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState == null) {
            _binding = FragmentFavoritesBinding.inflate(layoutInflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoritesState().observe(viewLifecycleOwner){
            when(it) {

                is MediaViewModel.FavoritesState.Empty -> {
                    binding.errorIv.isVisible = true
                    binding.errorTv.isVisible = true
                }
            }
        }
    }
}