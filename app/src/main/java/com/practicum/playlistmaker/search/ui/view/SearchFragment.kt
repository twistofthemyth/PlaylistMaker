package com.practicum.playlistmaker.search.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentSearchComposeBinding
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModel<SearchViewModel>()

    private var _binding: FragmentSearchComposeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchComposeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            SearchScreen(viewModel)
        }
        observeNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeNavigation() {
        viewModel.getTrackNavigationEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { track ->
                val direction =
                    SearchFragmentDirections.actionSearchFragmentToTrackFragment(track.trackId)
                findNavController().navigate(direction)
            })
    }
}