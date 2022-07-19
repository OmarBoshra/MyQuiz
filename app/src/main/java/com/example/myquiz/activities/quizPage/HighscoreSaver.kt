package com.example.myquiz.activities.quizPage

import android.content.Context
import android.content.SharedPreferences

class HighscoreSaver(context: Context, totalScore: Int) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    init {

        val pref = context.applicationContext.getSharedPreferences("MyPref", 0)
        // check if shared preferences exist , if not add a default key of Highscore
        if (!context.applicationContext.getSharedPreferences("MyPref", 0)
                .contains("HighestScore")
        ) {
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putInt("HighestScore", 0)
            editor.apply()
        }

        if (pref != null) {
            if (pref.getInt("HighestScore", 0) < totalScore) {
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putInt("HighestScore", totalScore)
                editor.apply()
            }
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {


    }
}