package com.example.pumlab7

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pumlab7.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var memoryGame: MemoryGame
    private lateinit var timer: CountDownTimer
    private var startTime = 0L
    private var elapsedTime = 0L
    private var difficulty = 4 // liczba par (4x2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGame()

        binding.resetButton.setOnClickListener {
            setupGame()
        }
        binding.highScoresButton.setOnClickListener {
            val intent = Intent(this, ScoresActivity::class.java)
            startActivity(intent)
        }

        binding.easyButton.setOnClickListener {
            difficulty = 4 // 4 pary (8 kart)
            setupGame()
        }

        binding.hardButton.setOnClickListener {
            difficulty = 8 // 8 par (16 kart)
            setupGame()
        }

    }

    private fun setupGame() {
        memoryGame = MemoryGame(difficulty)
        startTime = System.currentTimeMillis()
        startTimer()
        displayBoard()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000
                binding.timerText.text = "Czas: ${elapsedTime}s"
            }

            override fun onFinish() {}
        }.start()
    }

    private fun displayBoard() {
        binding.gridLayout.removeAllViews()
        val columns = 4
        binding.gridLayout.columnCount = columns

        memoryGame.cards.forEachIndexed { index, card ->
            val button = Button(this).apply {
                text = ""
                setOnClickListener {
                    memoryGame.flipCard(index)
                    updateButtons()
                    if (memoryGame.hasWon()) {
                        timer.cancel()
                        saveScore()
                        Toast.makeText(this@MainActivity, "Wygrana! Czas: $elapsedTime s", Toast.LENGTH_LONG).show()
                    }
                }
            }
            binding.gridLayout.addView(button)
        }
        updateButtons()
    }

    private fun updateButtons() {
        for (i in 0 until binding.gridLayout.childCount) {
            val button = binding.gridLayout.getChildAt(i) as Button
            val card = memoryGame.cards[i]
            button.text = if (card.isFaceUp || card.isMatched) card.value.toString() else ""
            button.isEnabled = !card.isMatched
        }
    }

    private fun saveScore() {
        val score = Score(0, elapsedTime, Date(), difficulty)
        Thread {
            ScoreDatabase.getDatabase(this).scoreDao().insert(score)
        }.start()
    }
}
