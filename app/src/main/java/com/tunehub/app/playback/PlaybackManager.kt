package com.tunehub.app.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.tunehub.app.playback.history.PlaybackHistoryItem
import com.tunehub.app.playback.history.PlaybackHistoryStore

class PlaybackManager(
    context: Context,
    private val historyStore: PlaybackHistoryStore = PlaybackHistoryStore(),
) {
    private val applicationContext = context.applicationContext

    val player: ExoPlayer = ExoPlayer.Builder(applicationContext).build()

    val mediaSession: MediaSession = MediaSession.Builder(applicationContext, player).build()

    private val notificationManager: PlayerNotificationManager =
        NotificationHelper.buildNotificationManager(applicationContext, player)

    init {
        notificationManager.setPlayer(player)
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val metadata = mediaItem?.mediaMetadata
                if (mediaItem != null && metadata != null) {
                    historyStore.add(
                        PlaybackHistoryItem(
                            id = mediaItem.mediaId,
                            title = metadata.title?.toString().orEmpty(),
                            artist = metadata.artist?.toString().orEmpty(),
                            artworkUrl = metadata.artworkUri?.toString().orEmpty(),
                        ),
                    )
                }
            }
        })
    }

    fun setQueue(items: List<MediaItem>, startIndex: Int = 0) {
        player.setMediaItems(items, startIndex, 0L)
        player.prepare()
    }

    fun play() {
        player.playWhenReady = true
    }

    fun pause() {
        player.pause()
    }

    fun setRepeatMode(mode: Int) {
        player.repeatMode = mode
    }

    fun getHistory(): List<PlaybackHistoryItem> = historyStore.getRecent()

    fun release() {
        notificationManager.setPlayer(null)
        mediaSession.release()
        player.release()
    }
}
