package com.practicum.playlistmaker.settings.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupViewModel()
        setupToolbar()
        setupInput()
        observeNavigation()
        observeThemeChange()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
    }

    private fun setupInput() {
        findViewById<TextView>(R.id.settings_share_tv).setOnClickListener {
            viewModel.navigateTo(
                SettingsViewModel.NavigationDestination.Share
            )
        }
        findViewById<TextView>(R.id.settings_support_tv).setOnClickListener {
            viewModel.navigateTo(
                SettingsViewModel.NavigationDestination.Support
            )
        }
        findViewById<TextView>(R.id.settings_agreement_tv).setOnClickListener {
            viewModel.navigateTo(
                SettingsViewModel.NavigationDestination.Agreement
            )
        }

        findViewById<SwitchMaterial>(R.id.settings_theme_button).apply {
            isChecked = viewModel.getScreenState().value?.theme == AppStyle.DARK
            setOnCheckedChangeListener { _, isChecked ->
                handler.postDelayed(
                    { viewModel.setTheme(if (isChecked) AppStyle.DARK else AppStyle.LIGHT) },
                    150
                )
            }
        }
    }

    private fun observeThemeChange() {
        viewModel.getChangeThemeEvent().observe(this, SingleLiveEventObserver { newTheme ->
            when (newTheme) {
                AppStyle.LIGHT -> (application as App).setLightTheme()
                AppStyle.DARK -> (application as App).setDarkTheme()
            }
        })
    }

    private fun observeNavigation() {
        viewModel.getNavigationEvent().observe(this, SingleLiveEventObserver { destination ->
            when (destination) {
                is SettingsViewModel.NavigationDestination.Share -> {
                    val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                        setData("smsto:".toUri())
                        putExtra("sms_body", getString(R.string.share_app_url))
                    }
                    startActivity(shareIntent)
                }

                is SettingsViewModel.NavigationDestination.Support -> {
                    val sendToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
                        setData("mailto:".toUri())
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_default_subject))
                        putExtra(Intent.EXTRA_TEXT, getString(R.string.support_default_text))
                    }

                    startActivity(sendToSupportIntent)
                }

                is SettingsViewModel.NavigationDestination.Agreement -> {
                    val viewAgreementIntent =
                        Intent(Intent.ACTION_VIEW, getString(R.string.agreement_url).toUri())
                    startActivity(viewAgreementIntent)
                }
            }
        })
    }
}