package com.practicum.playlistmaker.settings.ui.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setContentView(R.layout.activity_settings)
        setupToolbar()
        setupInput()
    }

    private fun setupViewModel() {
        viewModel = SettingsViewModel(application)
        viewModel.getState().observe(this) {
            when (it.theme) {
                AppStyle.LIGHT -> {
                    AppCompatDelegate.MODE_NIGHT_YES
                    findViewById<SwitchMaterial>(R.id.settings_theme_button).isChecked = true
                }

                AppStyle.DARK -> {
                    AppCompatDelegate.MODE_NIGHT_NO
                    findViewById<SwitchMaterial>(R.id.settings_theme_button).isChecked = false
                }
            }
        }
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
    }

    private fun setupInput() {
        findViewById<TextView>(R.id.settings_share_tv).setOnClickListener { viewModel.startShareIntent() }
        findViewById<TextView>(R.id.settings_support_tv).setOnClickListener { viewModel.startSupportIntent() }
        findViewById<TextView>(R.id.settings_agreement_tv).setOnClickListener { viewModel.startViewAgreementIntent() }
        findViewById<SwitchMaterial>(R.id.settings_theme_button).setOnClickListener { viewModel.toggleTheme() }
    }
}