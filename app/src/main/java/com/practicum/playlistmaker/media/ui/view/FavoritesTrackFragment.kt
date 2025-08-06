package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.player.ui.view.TrackFragment
import com.practicum.playlistmaker.search.ui.view.TrackListAdapter
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import com.practicum.playlistmaker.util.ui_utils.TrackNavigatableViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoritesTrackFragment : Fragment() {
    private val viewModel: MediaViewModel by activityViewModel<MediaViewModel>()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var _favoritesAdapter: TrackListAdapter? = null
    private val favoritesAdapter get() = _favoritesAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()

        viewModel.getFavoritesState().observe(viewLifecycleOwner) {
            when (it) {
                is MediaViewModel.FavoritesState.Empty -> {
                    binding.errorIv.isVisible = true
                    binding.errorTv.isVisible = true
                    binding.RvFavorites.isVisible = false
                }

                is MediaViewModel.FavoritesState.Content -> {
                    binding.errorIv.isVisible = false
                    binding.errorTv.isVisible = false
                    binding.RvFavorites.isVisible = true
                    favoritesAdapter.updateList(it.favoritesTrack)
                }
            }
        }

        viewModel.getNavigationEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { destination ->
                when (destination) {
                    is TrackNavigatableViewModel.NavigationDestination.ToTrack -> {
                        findNavController().navigate(
                            R.id.action_mediaFragment_to_trackFragment,
                            TrackFragment.createArgs(destination.track.trackId)
                        )
                    }
                }
            })
    }

    private fun setupRecyclerViews() {
        _favoritesAdapter = TrackListAdapter { track -> viewModel.clickTrack(track) }

        binding.RvFavorites.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _favoritesAdapter = null
    }
}