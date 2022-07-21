package com.example.myquiz.models

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper


class QuizPageUIData (

    //show on UIPage
    var questionImageUrl: String? = null,
    var currentQuestionIndex: Int = 0,
    var score: Int = 0,
    var totalScore: Int = 0,
    var islastQuestion: Boolean = false,
    var correctAnswer: String = "",
    var answersHashMap: HashMap<String, String> = HashMap(),
    var questionTimer: Handler = Handler(Looper.getMainLooper()),
    var numberOfQuestions:  Int = 0,
    var question: String? = null,


        ){

}