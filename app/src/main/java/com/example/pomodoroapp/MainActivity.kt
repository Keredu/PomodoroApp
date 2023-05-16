package com.example.pomodoroapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var mediaPlayer: MediaPlayer

    private val workDuration: Long = 25 * 60 * 1000 // 25 minutes in milliseconds
    private val breakDuration: Long = 5 * 60 * 1000 // 5 minutes in milliseconds

    private var timer: CountDownTimer? = null
    private var isWorking = true
    private var isPaused = true
    private var remainingTime = workDuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timerTextView)
        startButton = findViewById(R.id.startButton)
        resetButton = findViewById(R.id.resetButton)

        mediaPlayer = MediaPlayer.create(this, R.raw.song)

        startButton.setOnClickListener {
            if (isPaused) {
                startTimer()
            } else {
                pauseTimer()
            }
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        updateTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                if (isWorking) {
                    remainingTime = breakDuration
                    isWorking = false
                    playSong()
                    // TODO: Display "Take a break!" notification or custom UI
                } else {
                    remainingTime = workDuration
                    isWorking = true
                    playSong()
                    // TODO: Display "Get back to work!" notification or custom UI
                }
                updateTimer()
            }
        }.start()

        isPaused = false
    }

    private fun pauseTimer() {
        timer?.cancel()
        isPaused = true
    }

    private fun resetTimer() {
        timer?.cancel()
        isPaused = true
        remainingTime = workDuration
        isWorking = true
        updateTimer()
    }

    private fun updateTimer() {
        val minutes = (remainingTime / 1000) / 60
        val seconds = (remainingTime / 1000) % 60

        val displayText = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = displayText
    }

    private fun playSong() {
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
