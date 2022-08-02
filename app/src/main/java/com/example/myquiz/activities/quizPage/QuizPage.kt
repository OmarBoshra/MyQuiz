package com.example.myquiz.activities.quizPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.myquiz.activities.quizPage.adapters.QuizViewPagerAdapter
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
    lateinit var adapter: QuizViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize the UI binding
        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val items :ArrayList<Fragment> = arrayListOf(quizfragment())

        adapter = QuizViewPagerAdapter(1,this@QuizPage)


        binding.quizviewpager.adapter = adapter



    }

}

