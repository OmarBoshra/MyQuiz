package com.example.myquiz.activities.quizPage

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myquiz.activities.quizPage.adapters.QuizPagerAdapter
import com.example.myquiz.activities.quizPage.adapters.RecyclerViewAdapter
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.models.*


class QuizPage : AppCompatActivity() {
    /**
     * # QuizPage
     * The Page showing the quizQuestions
     * @param binding Binding for all the activities views
     * Manipulated By [renderQuestion]
     * @param dialogProgress progress dialog awaiting data fromviewModel
     * @param recyclerViewAdapter progress dialog awaiting data fromviewModel
     * View model data
     * @param quizPageViewModel the viewmodel object to be instantiated
     * @param quizPageUIData data coming from viewmodel
     * @param questionTimer handle the timing between every question as well as after answering
     * */

    private lateinit var binding: QuizPageBinding
    private lateinit var dialogProgress: Check24ProgressBar
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var quizPageViewModel: QuizPageViewModel
    private lateinit var quizPageUIData: QuizPageUIData
    private var questionTimer: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize the UI binding
        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items :ArrayList<Fragment> = arrayListOf(quizfragment())

       val adapter = QuizPagerAdapter(items,this@QuizPage)

        binding.quizviewpager.adapter = adapter

    }

}

