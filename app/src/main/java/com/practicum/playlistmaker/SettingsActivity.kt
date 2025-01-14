package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shareTv = findViewById<TextView>(R.id.settings_share_tv)
        val supportTv = findViewById<TextView>(R.id.settings_support_tv)
        val agreementTv = findViewById<TextView>(R.id.settings_agreement_tv)

        shareTv.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:")
                putExtra("sms_body", getString(R.string.share_app_url))
            }
            startActivity(shareIntent)
        }

        supportTv.setOnClickListener {
            val sendToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, getString(R.string.support_email))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_default_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_default_text))
            }

            startActivity(sendToSupportIntent)
        }

        agreementTv.setOnClickListener {
            val viewAgreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_url)))
            startActivity(viewAgreementIntent)
        }
    }
}