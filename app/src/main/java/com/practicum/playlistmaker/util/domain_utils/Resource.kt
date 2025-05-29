package com.practicum.playlistmaker.util.domain_utils

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class ClientError<T>(data: T? = null) : Resource<T>(data)
    class ServerError<T>(data: T? = null) : Resource<T>(data)
}