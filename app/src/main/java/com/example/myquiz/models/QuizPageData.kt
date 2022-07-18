package com.example.myquiz.models

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.example.myquiz.activities.quizPage.adapters.AnswersListAdapter
import java.util.HashMap

data class QuizPageData(


    // for the listViewListner
    var correctAnswer: String = "",
    var answersList: ArrayList<String> = arrayListOf(),
    var answersHashMap: HashMap<String, String>? = null,
    var questionTimer: Handler = Handler(Looper.getMainLooper()),

    // for the QuestionDataInitializer
    var adapter: AnswersListAdapter? = null,
    var answerResult: Boolean? = null,


    //show on UIPage
    var questionImageBitMap: Bitmap? = null,
    var currentQuestionIndex: Int = 0,
    var totalScore: Int = 0,
    var islastQuestion: Boolean = false

)


