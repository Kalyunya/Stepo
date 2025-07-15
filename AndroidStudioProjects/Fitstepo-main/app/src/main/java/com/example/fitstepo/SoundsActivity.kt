package com.example.fitstepo

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.TimeUnit

class SoundsActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnRepeat: ImageButton
    private lateinit var btnShuffle: ImageButton

    private lateinit var tvSongTitle: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var playlistRecyclerView: RecyclerView

    private lateinit var trackList: List<Track>
    private var currentTrackIndex: Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sounds)

        // Назад
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Ініціалізація елементів
        tvSongTitle = findViewById(R.id.tvSongTitle)
        tvArtistName = findViewById(R.id.tvArtistName)
        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvTotalTime = findViewById(R.id.tvTotalTime)
        seekBar = findViewById(R.id.seekBar)

        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnRepeat = findViewById(R.id.btnRepeat)
        btnShuffle = findViewById(R.id.btnShuffle)

        playlistRecyclerView = findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Плейлист (додай свої файли в res/raw)
        trackList = listOf(
            Track("Thunderstruck", "4:52", R.raw.thunderstruck),
            Track("Over The Hills And Far Away", "5:11", R.raw.nightwish_over),
            Track("Whiskey In The Jar", "5:04", R.raw.metallica_whiskey),
            Track("Aerials", "3:54", R.raw.s_o_a_d_aerials),
            Track("Toxicity", "3:39", R.raw.s_o_a_d_toxicity),
            Track("She Is My Sin", "4:46", R.raw.nightwish_she),
            Track("Deep Silent Complete", "4:00", R.raw.nightwish_deep),
            Track("Away", "4:39", R.raw.nightwish_away),
            Track("The Unforgiven", "6:29", R.raw.metallica_the_unforgiven),
            Track("Nothing Else Matters", "6:28", R.raw.metallica_nothing)
        )

        // Підключення адаптера
        playlistRecyclerView.adapter = TrackAdapter(trackList) { track ->
            currentTrackIndex = trackList.indexOf(track)
            playTrack(track)
        }

        // Стартовий трек
        currentTrackIndex = 0
        playTrack(trackList[currentTrackIndex])

        // Кнопка Play/Pause
        btnPlayPause.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    btnPlayPause.setImageResource(R.drawable.ic_play)
                    isPlaying = false
                } else {
                    it.start()
                    btnPlayPause.setImageResource(R.drawable.ic_pause)
                    isPlaying = true
                    updateSeekBar()
                }
            }
        }

        // Обробка перемотки
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
                tvCurrentTime.text = formatTime(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Обробка кнопок Next, Prev, Shuffle, Repeat
        btnStopLogic()
    }

    private fun playTrack(track: Track) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, track.resId)
        tvSongTitle.text = track.title
        tvArtistName.text = "Unknown Artist"

        mediaPlayer?.setOnPreparedListener {
            seekBar.max = it.duration
            tvTotalTime.text = formatTime(it.duration)
            it.start()
            btnPlayPause.setImageResource(R.drawable.ic_pause)
            isPlaying = true
            updateSeekBar()
        }
    }

    private fun btnStopLogic() {
        btnPrevious.setOnClickListener {
            if (currentTrackIndex > 0) {
                currentTrackIndex--
                playTrack(trackList[currentTrackIndex])
            }
        }

        btnNext.setOnClickListener {
            if (currentTrackIndex < trackList.size - 1) {
                currentTrackIndex++
                playTrack(trackList[currentTrackIndex])
            }
        }

        btnRepeat.setOnClickListener {
            mediaPlayer?.isLooping = !(mediaPlayer?.isLooping ?: false)
        }

        btnShuffle.setOnClickListener {
            currentTrackIndex = (trackList.indices).random()
            playTrack(trackList[currentTrackIndex])
        }
    }

    private fun updateSeekBar() {
        mediaPlayer?.let {
            seekBar.progress = it.currentPosition
            tvCurrentTime.text = formatTime(it.currentPosition)
            if (isPlaying) {
                handler.postDelayed({ updateSeekBar() }, 1000)
            }
        }
    }

    private fun formatTime(ms: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
