package com.practicum.playlistmaker.main.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.media.ui.view.MediaActivity
import com.practicum.playlistmaker.search.ui.view.SearchActivity
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.settings.ui.view.SettingsActivity
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupInput()
        observeNavigation()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java].apply {
            when (getState().value?.theme) {
                AppStyle.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                AppStyle.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                null -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun setupInput() {
        findViewById<Button>(R.id.search_button).setOnClickListener {
            viewModel.navigateTo(MainViewModel.NavigationDestination.Search)
        }

        findViewById<Button>(R.id.media_button).setOnClickListener {
            viewModel.navigateTo(MainViewModel.NavigationDestination.Media)
        }

        findViewById<Button>(R.id.settings_button).setOnClickListener {
            viewModel.navigateTo(MainViewModel.NavigationDestination.Settings)
        }
    }

    private fun observeNavigation() {
        viewModel.getNavigationEvent().observe(this, SingleLiveEventObserver { destination ->
            when (destination) {
                MainViewModel.NavigationDestination.Media -> startActivity(Intent(this, MediaActivity::class.java))
                MainViewModel.NavigationDestination.Search -> startActivity(Intent(this, SearchActivity::class.java))
                MainViewModel.NavigationDestination.Settings -> startActivity(Intent(this, SettingsActivity::class.java))
            }
        })
    }
}