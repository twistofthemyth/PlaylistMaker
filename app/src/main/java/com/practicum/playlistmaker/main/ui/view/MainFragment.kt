package com.practicum.playlistmaker.main.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.media.ui.view.MediaFragment
import com.practicum.playlistmaker.search.ui.view.SearchFragment
import com.practicum.playlistmaker.settings.ui.view.SettingsFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModel<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInput()
    }

    private fun setupInput() {
        binding.searchButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SearchFragment(), "SearchFragment")
                .addToBackStack("MainFragment")
                .setReorderingAllowed(true)
                .commit()
        }

        binding.mediaButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, MediaFragment(), "MediaFragment")
                .addToBackStack("MainFragment")
                .setReorderingAllowed(true)
                .commit()
        }

        binding.settingsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SettingsFragment(), "SettingsFragment")
                .addToBackStack("MainFragment")
                .setReorderingAllowed(true)
                .commit()
        }
    }
}