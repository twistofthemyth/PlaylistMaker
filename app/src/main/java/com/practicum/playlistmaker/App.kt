package com.practicum.playlistmaker

import android.app.Application
import android.graphics.PorterDuff
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(repositoryModule, dataModule, viewModelModule)
        }

        when (getKoin().get<SettingsInteractor>().getAppTheme()) {
            AppStyle.LIGHT -> setLightTheme()
            AppStyle.DARK -> setDarkTheme()
        }
    }

    fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun showToast(root: View, text: String) {
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(R.color.toast_background))
            .setBackgroundTintMode(PorterDuff.Mode.DST)
            .setTextColor(getColor(R.color.background))
            .setActionTextColor(getColor(R.color.background))
            .show()
    }
}