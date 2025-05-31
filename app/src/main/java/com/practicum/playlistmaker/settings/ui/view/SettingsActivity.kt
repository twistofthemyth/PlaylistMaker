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
        findViewById<TextView>(R.id.settings_share_tv).setOnClickListener { startShareIntent() }
        findViewById<TextView>(R.id.settings_support_tv).setOnClickListener { startSupportIntent() }
        findViewById<TextView>(R.id.settings_agreement_tv).setOnClickListener { startViewAgreementIntent() }
        findViewById<SwitchMaterial>(R.id.settings_theme_button).setOnClickListener { viewModel.toggleTheme() }
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