package com.example.pumlab5

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pumlab5.model.GameBoard
import com.example.pumlab5.utils.SoundManager

class GameActivity : AppCompatActivity() {
    private lateinit var board: GameBoard
    private lateinit var timer: CountDownTimer
    private lateinit var grid: GridLayout
    private lateinit var testWinBtn: Button
    private var moveCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val backBtn = findViewById<Button>(R.id.backToMenuButton)
        val shuffleBtn = findViewById<Button>(R.id.shuffleButton)
        testWinBtn = findViewById<Button>(R.id.testWinButton)

        backBtn.setOnClickListener {
            finish() // wraca do menu głównego
        }

        shuffleBtn.setOnClickListener {
            start()
        }



        start()
        handler.postDelayed(timerRunnable, 1000)
    }

    private var secondsElapsed = 0
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            secondsElapsed++
            findViewById<TextView>(R.id.timerView).text = "Czas: $secondsElapsed"
            handler.postDelayed(this, 1000)
        }
    }

    private fun start() {
        grid = findViewById(R.id.gridLayout)
        board = GameBoard(this, grid, ::onTileMoved, ::onGameWin)
        board.shuffle()

        testWinBtn.setOnClickListener {
            board.setTestWinState()
        }

        resetCounters()
//        startTimer()
    }


//    private fun startTimer() {
//        timer = object : CountDownTimer(9999999, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                findViewById<TextView>(R.id.timerView).text = "Czas: ${millisUntilFinished / 1000}"
//            }
//            override fun onFinish() {}
//        }
//        timer.start()
//    }

    private fun onTileMoved() {
        moveCount++
        findViewById<TextView>(R.id.moveCounter).text = "Ruchy: $moveCount"
        SoundManager.playMove(this)
    }

    private fun onGameWin() {
//        timer.cancel()
        SoundManager.playWin(this)
        val intent = Intent(this, EndGameActivity::class.java)
        intent.putExtra("moves", moveCount)
        startActivity(intent)
        finish()
    }

    private fun resetCounters() {
        moveCount = 0
        findViewById<TextView>(R.id.moveCounter).text = "Ruchy: $moveCount"
        secondsElapsed = 0
        findViewById<TextView>(R.id.timerView).text = "Czas: 0"
    }




    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
    }

}
