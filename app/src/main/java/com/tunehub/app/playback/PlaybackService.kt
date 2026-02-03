package com.tunehub.app.playback

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {
    private lateinit var playbackManager: PlaybackManager

    override fun onCreate() {
        super.onCreate()
        playbackManager = PlaybackManager(this)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return playbackManager.mediaSession
    }

    override fun onDestroy() {
        playbackManager.release()
        super.onDestroy()
    }
}
