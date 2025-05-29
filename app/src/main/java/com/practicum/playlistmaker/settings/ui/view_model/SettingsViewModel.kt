package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsInteractor = Creator.provideSettingsInteractor(
        application.getSharedPreferences(
            "SettingsViewMode",
            MODE_PRIVATE
        )
    )

    private val state = MutableLiveData<SettingsState>(getDefaultSettings())


    fun getState(): LiveData<SettingsState> = state

    fun toggleTheme() {
        when (state.value?.theme) {
            AppStyle.LIGHT -> {
                state.value?.theme = AppStyle.DARK
                settingsInteractor.changeAppTheme(AppStyle.DARK)
            }

            AppStyle.DARK -> {
                state.value?.theme = AppStyle.LIGHT
                settingsInteractor.changeAppTheme(AppStyle.LIGHT)
            }

            null -> {
                state.value = getDefaultSettings()
            }
        }
    }

    fun startShareIntent() {
        val app = getApplication<App>()
        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
            setData("smsto:".toUri())
            putExtra("sms_body", app.getString(R.string.share_app_url))
        }
        app.startActivity(shareIntent)
    }

    fun startSupportIntent() {
        val app = getApplication<App>()
        val sendToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
            setData("mailto:".toUri())
            putExtra(Intent.EXTRA_EMAIL, arrayOf(app.getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, app.getString(R.string.support_default_subject))
            putExtra(Intent.EXTRA_TEXT, app.getString(R.string.support_default_text))
        }

        app.startActivity(sendToSupportIntent)
    }

    fun startViewAgreementIntent() {
        val app = getApplication<App>()
        val viewAgreementIntent =
            Intent(Intent.ACTION_VIEW, app.getString(R.string.agreement_url).toUri())
        app.startActivity(viewAgreementIntent)
    }

    private fun getDefaultSettings(): SettingsState {
        return SettingsState(settingsInteractor.getAppTheme())
    }

    data class SettingsState(var theme: AppStyle);
}