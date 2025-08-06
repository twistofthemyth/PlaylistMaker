package com.practicum.playlistmaker.search.ui.view

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.view.TrackFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import com.practicum.playlistmaker.util.ui_utils.TrackNavigatableViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var _searchAdapter: TrackListAdapter? = null
    private val searchAdapter get() = _searchAdapter!!

    private var _searchHistoryAdapter: TrackListAdapter? = null
    private val searchHistoryAdapter get() = _searchHistoryAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchInput()
        setupRecyclerViews()
        setupViewModel()
        observeNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchAdapter = null
        _searchHistoryAdapter = null
    }

    private fun setupViewModel() {
        viewModel.getScreenState().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchViewModel.SearchViewState.Loading -> {
                    showLoading()
                    hideTracks()
                    hideErrors()
                    hideTrackHistory()
                }

                is SearchViewModel.SearchViewState.NetworkError -> {
                    showNetworkError()
                    hideLoading()
                    hideTracks()
                    hideTrackHistory()
                }

                is SearchViewModel.SearchViewState.ShowHistory -> {
                    binding.searchEt.apply {
                        setText("")
                        hideKeyboard(this)
                    }
                    hideTracks()
                    hideErrors()
                    if (screenState.tracks.isEmpty()) {
                        hideTrackHistory()
                    } else {
                        updateTrackHistory(screenState.tracks)
                        showTrackHistory()
                    }
                }

                is SearchViewModel.SearchViewState.ShowSearchResult -> {
                    hideLoading()
                    hideErrors()
                    hideTrackHistory()
                    if (screenState.tracks.isEmpty()) {
                        showNotFoundError()
                    } else {
                        showTracks(screenState.tracks)
                    }
                }

                is SearchViewModel.SearchViewState.InitedSearchInput -> {
                    showClearQueryButton()
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        _searchAdapter = TrackListAdapter { track -> clickTrack(track) }
        _searchHistoryAdapter = TrackListAdapter { track -> clickTrack(track) }

        binding.RvSearchResult.apply {
            adapter = _searchAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        binding.RvSearchHistory.apply {
            adapter = _searchHistoryAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupSearchInput() {
        binding.searchEt.apply {
            doOnTextChanged { text, _, _, _ ->
                if (text?.isEmpty() == true) {
                    hideClearQueryButton()
                } else {
                    showClearQueryButton()
                    viewModel.search(text.toString())
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.search(text.toString())
                }
            }
        }

        binding.errorUpdateBtn.setOnClickListener { viewModel.repeatSearch() }
        binding.clearHistoryBtn.setOnClickListener { viewModel.cleanHistory() }
        binding.clearSearchIv.setOnClickListener { viewModel.cleanSearchQuery() }
    }

    private fun showLoading() {
        binding.searchPb.isVisible = true
    }

    private fun hideLoading() {
        binding.searchPb.isVisible = false
    }

    private fun showNetworkError() {
        binding.errorIv.setImageResource(R.drawable.placeholder_net_error)
        binding.errorTv.setText(R.string.net_error)
        binding.errorUpdateBtn.isVisible = true
        binding.errorLl.isVisible = true
    }

    private fun showNotFoundError() {
        binding.errorIv.setImageResource(R.drawable.placeholder_not_found)
        binding.errorTv.setText(R.string.not_found)
        binding.errorUpdateBtn.isVisible = false
        binding.errorLl.isVisible = true
    }

    private fun hideErrors() {
        binding.errorLl.isVisible = false
    }

    private fun showTracks(tracks: List<Track>) {
        searchAdapter.updateList(tracks)
        binding.RvSearchResult.isVisible = true
    }

    private fun hideTracks() {
        searchAdapter.updateList(listOf())
        binding.RvSearchResult.isVisible = false
    }

    private fun showTrackHistory() {
        binding.searchEt.apply {
            setText("")
            clearFocus()
        }
        binding.searchHistoryLl.isVisible = true
    }

    private fun updateTrackHistory(tracks: List<Track>) {
        searchHistoryAdapter.updateList(tracks)
    }

    private fun hideTrackHistory() {
        binding.searchHistoryLl.isVisible = false
    }

    private fun showClearQueryButton() {
        binding.clearSearchIv.isVisible = true
    }

    private fun hideClearQueryButton() {
        binding.clearSearchIv.isVisible = false
    }

    private fun clickTrack(track: Track) {
        viewModel.clickTrack(track)
    }

    private fun observeNavigation() {
        viewModel.getNavigationEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { destination ->
                when (destination) {
                    is TrackNavigatableViewModel.NavigationDestination.ToTrack -> {
                        findNavController().navigate(R.id.action_searchFragment_to_trackFragment,
                            TrackFragment.createArgs(destination.track.trackId))
                    }
                }
            })
    }

    private fun hideKeyboard(view: View) {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}