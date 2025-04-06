package com.example.pumlab5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EndGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        val moves = intent.getIntExtra("moves", 0)
        findViewById<TextView>(R.id.scoreText).text = "Wygrałeś! Ruchy: $moves"

        findViewById<Button>(R.id.menuButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
