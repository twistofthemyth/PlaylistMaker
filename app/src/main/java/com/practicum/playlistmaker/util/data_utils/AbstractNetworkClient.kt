package com.practicum.playlistmaker.util.data_utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

abstract class AbstractNetworkClient(val connectivityManager: ConnectivityManager) : NetworkClient {
    protected fun isConnected(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}