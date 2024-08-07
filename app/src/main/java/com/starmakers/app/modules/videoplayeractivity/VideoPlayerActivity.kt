package com.starmakers.app.modules.videoplayeractivity

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.media3.ui.PlayerView
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.starmakers.app.R

class VideoPlayerActivity : AppCompatActivity() {

    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var playerView: SimpleExoPlayerView
    private var videoUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Lock orientation to landscape
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Retrieve video URL from intent
        videoUrl = intent.getStringExtra("videoUrl")

        playerView = findViewById(R.id.playerView)

        playerView.showController()

        playerView.useController=true


        window.statusBarColor= ContextCompat.getColor(this,R.color.statusbar2)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            val trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            val loadControl = DefaultLoadControl()
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
            //playerView.player = exoPlayer

            val dataSourceFactory = DefaultHttpDataSourceFactory(
                "ExoPlayer",null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true
            )
            val extractorsFactory = DefaultExtractorsFactory()
            val videoUri = Uri.parse(videoUrl)
            val mediaSource = ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null)


            playerView.player = exoPlayer

            exoPlayer?.prepare(mediaSource)
            exoPlayer?.playWhenReady = true



        }
    }
    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

}