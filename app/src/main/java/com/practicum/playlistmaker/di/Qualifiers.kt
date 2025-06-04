package com.practicum.playlistmaker.di

import org.koin.core.qualifier.StringQualifier

object Qualifiers {
    val trackForPlaying = StringQualifier("trackForPlaying")
}