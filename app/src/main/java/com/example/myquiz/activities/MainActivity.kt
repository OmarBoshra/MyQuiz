package com.example.myquiz.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myquiz.R
import com.example.myquiz.activities.quizPage.QuizPage
import com.example.myquiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        val highestScore = pref.getInt("HighestScore", 0)
        val highestScoreView = resources.getString(R.string.highscore, highestScore)
        binding.highscoreTextview.text = highestScoreView

        binding.startButton.setOnClickListener {
            val navigtionIntent = Intent(this@MainActivity, QuizPage::class.java)
            startActivity(navigtionIntent)
        }
    }
}