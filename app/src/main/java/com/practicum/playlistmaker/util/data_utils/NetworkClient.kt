package com.practicum.playlistmaker.util.data_utils

import com.practicum.playlistmaker.util.data_utils.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}