package com.practicum.playlistmaker.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MusicPlayerService() : Service(), KoinComponent, IMusicPlayerService {

    companion object {
        const val TRACK_URL_TAG = "track_url"
        const val TRACK_NAME_TAG = "track_name"
        const val TRACK_ARTIST_TAG = "track_artist"
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 100
        private const val TRACK_UPDATE_DELAY = 300L
    }

    private val trackPlayer: TrackPlayer by inject()
    private val binder = MusicServiceBinder()
    private var timerJob: Job? = null

    private var trackUrl: String? = null
    private var trackName: String? = null
    private var trackArtist: String? = null

    private var _listener: MusicPlayerListener? = null


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        trackUrl = intent?.getStringExtra(TRACK_URL_TAG)
        trackName = intent?.getStringExtra(TRACK_NAME_TAG)
        trackArtist = intent?.getStringExtra(TRACK_ARTIST_TAG)

        if (trackUrl == null || trackName == null || trackArtist == null) {
            throw IllegalStateException()
        }
        trackPlayer.preparePlayer(trackUrl!!)
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        trackPlayer.releasePlayer()
        _listener = null
        return super.onUnbind(intent)
    }

    override fun startPlayer() {
        trackPlayer.startPlayer()
        startTimer()
    }

    override fun stopPlayer() {
        trackPlayer.stopPlayer()
        timerJob?.cancel()
    }

    override fun setListener(listener: MusicPlayerListener) {
        _listener = listener
    }

    override fun startForeground() {
        if(trackPlayer.getState() == PlayerState.STATE_PLAYING) {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        }
    }

    override fun stopForeground() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (trackPlayer.getState() == PlayerState.STATE_PLAYING) {
                delay(TRACK_UPDATE_DELAY)
                _listener?.onStateChanged(MusicPlayerState(trackPlayer.getPosition(), false))
            }
            _listener?.onStateChanged(MusicPlayerState(trackPlayer.getPosition(), true))
            stopForeground()
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Playlist Maker")
            .setContentText("$trackArtist - $trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
}