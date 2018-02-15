package com.example.jaeyoungpark.loudnessenhancer

import android.net.Uri
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mPlayer: SimpleExoPlayer
    private var mLoopStartNEnd: Pair<Long, Long> = Pair(-1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val uri: Uri = getUri()
        initPlayer(uri)
        loopBetween(0, 3)
    }

    private fun loopBetween(start: Long, end: Long) {
        mLoopStartNEnd = Pair(start, end)
    }

    private fun notifyCurrentPosition(currentPosition: Long) {
        Log.d(TAG, "notifyCurrentPosition() called  with: currentPosition = [$currentPosition]")

        if (mLoopStartNEnd.first != (-1).toLong() && mLoopStartNEnd.second != (-1).toLong()) {
            if (mLoopStartNEnd.second < currentPosition) {
                mPlayer.seekTo(mLoopStartNEnd.first)
            }

        }
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

        mPlayer.addListener(object: ExoPlayer.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

            }

            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

            }

            override fun onPlayerError(error: ExoPlaybackException?) {

            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

            }

            override fun onLoadingChanged(isLoading: Boolean) {

            }

            override fun onPositionDiscontinuity() {
                Log.d(TAG, "onPositionDiscontinuity: ")

                notifyCurrentPosition(mPlayer.currentPosition)
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {

            }
        })
    }
}
