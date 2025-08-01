package com.cristiandiaz.cazapatos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var textViewUser: TextView
    private lateinit var textViewCounter: TextView
    private lateinit var textViewTime: TextView
    private lateinit var imageViewDuck: ImageView
    private lateinit var soundPool: SoundPool
    private val handler = Handler(Looper.getMainLooper())
    private var counter = 0
    private var screenWidth = 0
    private var screenHeight = 0
    private var soundId: Int = 0
    private var isLoaded = false
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textViewUser = findViewById(R.id.textViewUser)
        textViewCounter = findViewById(R.id.textViewCounter)
        textViewTime = findViewById(R.id.textViewTime)
        imageViewDuck = findViewById(R.id.imageViewDuck)

        val extras = intent.extras ?: return
        var usuario = extras.getString(EXTRA_LOGIN) ?:"Unknown"
        textViewUser.setText(usuario)

        initializeScreen()
        initializeCountdown()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(10)
            .build()
        soundId = soundPool.load(this, R.raw.gunshot, 1)
        soundPool.setOnLoadCompleteListener { _, _, _ ->
            isLoaded = true
        }

        imageViewDuck.setOnClickListener {
            if (gameOver) return@setOnClickListener
            counter++
            if (isLoaded) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            }
            textViewCounter.setText(counter.toString())
            imageViewDuck.setImageResource(R.drawable.duck_clicked)

            handler.postDelayed({
                imageViewDuck.setImageResource(R.drawable.duck)
            }, 500)
            moveDuckRandomly()
        }
    }

    private fun initializeScreen() {
        val display = this.resources.displayMetrics
        screenWidth = display.widthPixels
        screenHeight = display.heightPixels
    }

    private fun moveDuckRandomly() {
        val min = imageViewDuck.width / 2
        val maximoX = screenWidth - imageViewDuck.width
        val maximoY = screenHeight - imageViewDuck.height
        val randomX = Random.nextInt(0, maximoX - min + 1)
        val randomY = Random.nextInt(0, maximoY - min + 1)

        imageViewDuck.animate()
            .x(randomX.toFloat())
            .y(randomY.toFloat())
            .setDuration(300)
            .start()
    }

    private val countDownTimer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = millisUntilFinished / 1000
            textViewTime.setText("${secondsRemaining}s")
        }
        override fun onFinish() {
            textViewTime.setText("0s")
            gameOver = true
            showGameOverDialog()
        }
    }

    private fun initializeCountdown() {
        countDownTimer.start()
    }

    private fun showGameOverDialog() {
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage(getString(R.string.dialog_message_congratulations, counter))
            .setTitle(getString(R.string.dialog_title_game_end))
            .setPositiveButton(getString(R.string.button_restart)) { _, _ ->
                restartGame()
            }
            .setNegativeButton(getString(R.string.button_close)) { _, _ ->
                val intent = Intent(this, RankingActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
        builder.create().show()
    }

    private fun restartGame(){
        counter = 0
        gameOver = false
        countDownTimer.cancel()
        textViewCounter.setText(counter.toString())
        moveDuckRandomly()
        initializeCountdown()
    }

    override fun onStop() {
        super.onStop()
        if (gameOver) {
            return
        }
        Log.w(EXTRA_LOGIN, "Play canceled by user")
        countDownTimer.cancel()
        textViewTime.text = "0s"
        gameOver = true
        soundPool.stop(soundId)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
        countDownTimer.cancel()
    }
}