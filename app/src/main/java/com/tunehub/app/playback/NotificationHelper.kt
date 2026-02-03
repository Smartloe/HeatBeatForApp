package com.tunehub.app.playback

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.ui.PlayerNotificationManager

object NotificationHelper {
    private const val CHANNEL_ID = "tunehub_playback"
    private const val CHANNEL_NAME = "TuneHub Playback"
    private const val NOTIFICATION_ID = 1001

    fun buildNotificationManager(
        context: Context,
        player: Player,
    ): PlayerNotificationManager {
        ensureChannel(context)
        return PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID)
            .setMediaDescriptionAdapter(SimpleDescriptionAdapter(context))
            .setNotificationListener(SimpleNotificationListener())
            .setSmallIconResourceId(android.R.drawable.ic_media_play)
            .build()
            .apply {
                setPlayer(player)
                setUseRewindAction(false)
                setUseFastForwardAction(false)
                setPriority(NotificationCompat.PRIORITY_LOW)
            }
    }

    private fun ensureChannel(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(CHANNEL_ID) != null) {
            return
        }
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW,
        )
        manager.createNotificationChannel(channel)
    }
}
