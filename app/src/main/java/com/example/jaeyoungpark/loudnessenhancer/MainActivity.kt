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
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.LogManager

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mPlayer: SimpleExoPlayer
    private var mLoopStartNEnd: Pair<Long, Long> = Pair(-1, -1)
    private var mDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val uri: Uri = getUri().also { Log.d(TAG, "onCreate() called  with: it = [$it]") }
        initPlayer(uri)
        loopBetween(3000, 5000)
    }

    private fun loopBetween(start: Long, end: Long) {
        mPlayer.seekTo(start)
        mLoopStartNEnd = Pair(start, end)
    }

    private fun getUri(): Uri {
        Log.d(TAG, "getUri: ")
        val dir: File = Environment.getExternalStoragePublicDirectory("Download")
        dir.listFiles().asIterable()
//                .sortedByDescending { Random().nextInt() }
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

        mDisposable.add(Observable.interval(100, TimeUnit.MILLISECONDS)
                .map { mPlayer }
                .filter { !it.isLoading }
                .map { it.to(mLoopStartNEnd) }
                .filter { (player, loopStartNEnd) -> loopStartNEnd.first != (-1).toLong() && loopStartNEnd.second != (-1).toLong() }
                .filter { (player, loopStartNEnd) -> loopStartNEnd.second < player.currentPosition }
                .doOnNext { Log.d(TAG, "initPlayer() called it=$it") }
                .subscribe { (player, loopStartNEnd) -> player.seekTo(loopStartNEnd.first) })

        mPlayer.addListener(object: ExoPlayer.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                Log.d(TAG, "onPlaybackParametersChanged: ")

            }

            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                Log.d(TAG, "onTracksChanged: ")

            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                Log.d(TAG, "onPlayerError: ")

            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                Log.d(TAG, "onPlayerStateChanged() called  with: playWhenReady = [$playWhenReady], playbackState = [$playbackState]")


            }

            override fun onLoadingChanged(isLoading: Boolean) {
                Log.d(TAG, "onLoadingChanged() called  with: isLoading = [$isLoading]")

            }

            override fun onPositionDiscontinuity() {
                Log.d(TAG, "onPositionDiscontinuity: ")

            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
                Log.d(TAG, "onTimelineChanged: ")

            }
        })
    }
}
