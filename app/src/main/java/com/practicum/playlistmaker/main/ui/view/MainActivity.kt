package com.practicum.playlistmaker.main.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.media.ui.view.MediaActivity
import com.practicum.playlistmaker.search.ui.view.SearchActivity
import com.practicum.playlistmaker.settings.ui.view.SettingsActivity
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInput()
        observeNavigation()
    }

    private fun setupInput() {
        binding.searchButton.setOnClickListener {
            viewModel.navigateTo(MainViewModel.NavigationDestination.Search)
        }

        binding.mediaButton.setOnClickListener {
            viewModel.navigateTo(MainViewModel.NavigationDestination.Media)
        }

        binding.settingsButton.setOnClickListener {
            viewModel.navigateTo(MainViewModel.NavigationDestination.Settings)
        }
    }

    private fun observeNavigation() {
        viewModel.getNavigationEvent().observe(this, SingleLiveEventObserver { destination ->
            when (destination) {
                MainViewModel.NavigationDestination.Media -> startActivity(
                    Intent(
                        this,
                        MediaActivity::class.java
                    )
                )

                MainViewModel.NavigationDestination.Search -> startActivity(
                    Intent(
                        this,
                        SearchActivity::class.java
                    )
                )

                MainViewModel.NavigationDestination.Settings -> startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java
                    )
                )
            }
        })
    }
}