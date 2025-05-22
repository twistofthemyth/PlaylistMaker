package com.practicum.playlistmaker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupToolbar()
        setupInput()
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun setupInput() {
        findViewById<TextView>(R.id.settings_share_tv).apply {
            setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "smsto:".toUri()
                    putExtra("sms_body", getString(R.string.share_app_url))
                }
                startActivity(shareIntent)
            }
        }

        findViewById<TextView>(R.id.settings_support_tv).apply {
            setOnClickListener {
                val sendToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_default_subject))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.support_default_text))
                }

                startActivity(sendToSupportIntent)
            }
        }

        findViewById<TextView>(R.id.settings_agreement_tv).apply {
            setOnClickListener {
                val viewAgreementIntent =
                    Intent(Intent.ACTION_VIEW, getString(R.string.agreement_url).toUri())
                startActivity(viewAgreementIntent)
            }
        }


        findViewById<SwitchMaterial>(R.id.settings_theme_button).apply {
            isChecked = AppCompatDelegate.getDefaultNightMode().equals(MODE_NIGHT_YES)

            setOnCheckedChangeListener { switcher, checked ->
                (applicationContext as App).switchTheme(checked)
            }

            setOnClickListener {
                val theme = if (!isChecked) MODE_NIGHT_FOLLOW_SYSTEM else MODE_NIGHT_YES
                AppCompatDelegate.setDefaultNightMode(theme)
            }
        }
    }
}