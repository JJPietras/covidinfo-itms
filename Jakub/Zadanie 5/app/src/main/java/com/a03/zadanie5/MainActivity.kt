package com.a03.zadanie5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : YouTubeBaseActivity() {

    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var onInitializedListener: YouTubePlayer.OnInitializedListener

    private var youTubePlayerReference: YouTubePlayer? = null

    private lateinit var ftimsBtn: Button
    private lateinit var rickBtn: Button
    private lateinit var julianBtn: Button

    private lateinit var currentVideo: TextView

    private lateinit var playBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var stopBtn: Button

    private val videoTitles =
        arrayListOf("FTIMS CG Showreel", "Never Gonna Give You Up", "Ostry film zaangażowany")

    private val videos = arrayOf("Il1WDvX-qrg", "dQw4w9WgXcQ", "9uxlXIlihWA")

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentVideo = findViewById(R.id.currentMovie)

        ftimsBtn = findViewById(R.id.ftimsBtn)
        rickBtn = findViewById(R.id.rickBtn)
        julianBtn = findViewById(R.id.julianAntoniszBtn)

        ftimsBtn.setOnClickListener { changeMovie(0) }
        rickBtn.setOnClickListener { changeMovie(1) }
        julianBtn.setOnClickListener { changeMovie(2) }


        youTubePlayerView = findViewById(R.id.youtube_view)
        onInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                youTubePlayer: YouTubePlayer,
                b: Boolean
            ) {
                youTubePlayerReference = youTubePlayer
                youTubePlayerReference?.setPlayerStateChangeListener(object :
                    YouTubePlayer.PlayerStateChangeListener {
                    override fun onLoading() {
                        youTubePlayerReference?.pause()
                    }

                    override fun onLoaded(p0: String?) {}
                    override fun onAdStarted() {}
                    override fun onVideoStarted() {}
                    override fun onVideoEnded() {}
                    override fun onError(p0: YouTubePlayer.ErrorReason?) {}
                })
                youTubePlayer.loadVideo(videos[index])
                youTubePlayer.pause()
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider,
                youTubeInitializationResult: YouTubeInitializationResult
            ) {
                currentVideo.text = "Something went wrong - could not load selected video"
            }
        }

        playBtn = findViewById(R.id.playBtn)
        pauseBtn = findViewById(R.id.pauseBtn)
        stopBtn = findViewById(R.id.stopBtn)

        playBtn.setOnClickListener { youTubePlayerReference?.play() }
        pauseBtn.setOnClickListener { youTubePlayerReference?.pause() }
        stopBtn.setOnClickListener {
            youTubePlayerReference?.pause()
            youTubePlayerReference?.seekToMillis(0)
        }


        changeMovie(0)

    }

    private fun changeMovie(newIndex: Int) {
        index = newIndex
        currentVideo.text = videoTitles[index]
        youTubePlayerReference?.release()
        youTubePlayerView.initialize(
            "AIzaSyAt5WL4pS9ph7fOwHcjR86uIFBFPvHEbCU",
            onInitializedListener
        )
    }
}