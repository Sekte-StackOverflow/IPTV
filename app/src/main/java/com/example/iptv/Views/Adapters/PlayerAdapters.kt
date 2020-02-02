package com.example.iptv.Views.Adapters

import android.content.Context
import android.net.Uri
import android.os.Handler
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util

class PlayerAdapters(val context: Context, private val uri: String){

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    private var playbackPosition = 0L
    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelection by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }

    fun prepareExoPlayer() {
        val mUri = Uri.parse(uri)
        val mediaSource = hlsMediaSource(mUri)
        simpleExoPlayer.prepare(mediaSource)
    }

    fun releaseExoPlayer() {
        playbackPosition = simpleExoPlayer.currentPosition
        simpleExoPlayer.release()
    }

    private fun getMediaSource(uri: Uri) : MediaSource {
        val appName = Util.getUserAgent(
            context,
            context.applicationInfo.loadLabel(context.packageManager) as String
        )

        val dataSource: DataSource.Factory = DefaultDataSourceFactory(
            context, appName, bandwidthMeter
        )

        val mediaSource: MediaSource = HlsMediaSource.Factory(dataSource).createMediaSource(uri)
        return mediaSource
    }

    private fun hlsMediaSource(uri: Uri): HlsMediaSource {
        val appName = Util.getUserAgent(
            context,
            context.applicationInfo.loadLabel(context.packageManager) as String
            )

        val dataSourceFactory = DefaultHttpDataSourceFactory(
            appName,
            bandwidthMeter,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
            )

        return HlsMediaSource.Factory(dataSourceFactory).setAllowChunklessPreparation(true).createMediaSource(uri)
    }

    fun initExoPlayer(): SimpleExoPlayer {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
            context.applicationContext,
            DefaultRenderersFactory(context),
            DefaultTrackSelector(adaptiveTrackSelection),
            DefaultLoadControl()
        )
        prepareExoPlayer()
        return simpleExoPlayer
    }

    fun getPlayer(): SimpleExoPlayer {
        return simpleExoPlayer
    }

    fun playbackPostition(): Long {
        return playbackPosition
    }

    fun playVideo() {
        simpleExoPlayer.seekTo(playbackPosition)
        simpleExoPlayer.playWhenReady = true
    }

    fun stopVideo() {
        simpleExoPlayer.playWhenReady = false
        playbackPosition = simpleExoPlayer.currentPosition
    }


}