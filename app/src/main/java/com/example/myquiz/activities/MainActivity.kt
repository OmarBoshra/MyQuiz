package com.example.myquiz.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.myquiz.activities.quizPage.QuizPage
import com.example.myquiz.databinding.ActivityMainBinding
import com.example.myquiz.services.BackgroundIntentService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mQueue: RequestQueue? = null
    private lateinit var pref: SharedPreferences

    private val backgroundIntentServiceAction = "android.intent.action.CUSTOME_ACTION_1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mQueue = Volley.newRequestQueue(this@MainActivity)

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        val highestScore = pref.getInt("HighestScore", 0)
        val highestScoreView: StringBuilder = StringBuilder()
        highestScoreView.append("HighScore : ")
        highestScoreView.append(highestScore)
        binding.highscoreTextview.text = highestScoreView


        binding.startButton.setOnClickListener {


            // Handler code here.

            val serviceIntent =
                Intent(this@MainActivity, BackgroundIntentService::class.java).apply {
                    intent.action = backgroundIntentServiceAction

                }

            startService(serviceIntent)


            val navigtionIntent = Intent(this@MainActivity, QuizPage::class.java)
            startActivity(navigtionIntent)


        }


    }


}