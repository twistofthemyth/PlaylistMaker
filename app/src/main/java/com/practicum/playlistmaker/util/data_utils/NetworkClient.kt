package com.practicum.playlistmaker.util.data_utils

import com.practicum.playlistmaker.util.data_utils.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}