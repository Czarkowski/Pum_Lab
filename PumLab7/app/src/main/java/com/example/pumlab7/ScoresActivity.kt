package com.example.pumlab7

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pumlab7.databinding.ActivityScoresBinding

class ScoresActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScoresBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = ScoreDatabase.getDatabase(this)
        db.scoreDao().getBestScores().observe(this) { scores ->
            adapter = ScoreAdapter(scores)
            recyclerView.adapter = adapter
        }

        binding.backButton.setOnClickListener {
            finish() // zamyka aktywność i wraca do MainActivity
        }

    }
}
