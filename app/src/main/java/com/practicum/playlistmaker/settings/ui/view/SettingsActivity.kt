package com.practicum.playlistmaker.settings.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupInput()
        observeNavigation()
        observeThemeChange()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupInput() {
        binding.settingsShareTv.setOnClickListener {
            viewModel.navigateTo(
                SettingsViewModel.NavigationDestination.Share
            )
        }

        binding.settingsSupportTv.setOnClickListener {
            viewModel.navigateTo(
                SettingsViewModel.NavigationDestination.Support
            )
        }

        binding.settingsAgreementTv.setOnClickListener {
            viewModel.navigateTo(
                SettingsViewModel.NavigationDestination.Agreement
            )
        }

        binding.settingsThemeButton.apply {
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