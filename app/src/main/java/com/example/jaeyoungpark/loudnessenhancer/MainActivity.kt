package com.example.jaeyoungpark.loudnessenhancer

import android.net.Uri
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val uri: Uri = getUri()
        initPlayer(uri)
    }

    private fun getUri(): Uri {
        Log.d(TAG, "getUri: ")
        val dir: File = Environment.getExternalStoragePublicDirectory("Download")
        dir.listFiles().asIterable()
                .forEach {
                    if (it.extension == "m4a") {
                        return Uri.fromFile(it)
                    }
                }

        return Uri.EMPTY
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
