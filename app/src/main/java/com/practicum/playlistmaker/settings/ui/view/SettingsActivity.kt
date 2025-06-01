package com.practicum.playlistmaker.settings.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupViewModel()
        setupToolbar()
        setupInput()
    }

    private fun setupViewModel() {
        viewModel = SettingsViewModel(application)
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
    }

    private fun setupInput() {
        findViewById<TextView>(R.id.settings_share_tv).setOnClickListener { startShareIntent() }
        findViewById<TextView>(R.id.settings_support_tv).setOnClickListener { startSupportIntent() }
        findViewById<TextView>(R.id.settings_agreement_tv).setOnClickListener { startViewAgreementIntent() }

        findViewById<SwitchMaterial>(R.id.settings_theme_button).apply {
            isChecked = viewModel.getState().value?.theme == AppStyle.DARK

            setOnCheckedChangeListener { _, isChecked ->
                viewModel.setTheme(if (isChecked) AppStyle.DARK else AppStyle.LIGHT)
                when(viewModel.getState().value?.theme) {
                    AppStyle.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    AppStyle.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    null -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    private fun startShareIntent() {
        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
            setData("smsto:".toUri())
            putExtra("sms_body", getString(R.string.share_app_url))
        }
        startActivity(shareIntent)
    }

    private fun startSupportIntent() {
        val sendToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
            setData("mailto:".toUri())
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_default_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_default_text))
        }

        startActivity(sendToSupportIntent)
    }

    private fun startViewAgreementIntent() {
        val viewAgreementIntent =
            Intent(Intent.ACTION_VIEW, getString(R.string.agreement_url).toUri())
        startActivity(viewAgreementIntent)
    }
}