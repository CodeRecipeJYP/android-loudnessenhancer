package com.example.jaeyoungpark.loudnessenhancer

import android.net.Uri
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uri: Uri = Uri.EMPTY
        initPlayer(uri)
    }

    private fun initPlayer(uri: Uri) {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(),
                DefaultLoadControl())

        mPlayer.playWhenReady = true

        val mediaSource = ExtractorMediaSource(uri,
                DefaultDataSourceFactory(this, "ua"),
                DefaultExtractorsFactory(), null, null)

        mPlayer.prepare(mediaSource, true, false)
    }
}
