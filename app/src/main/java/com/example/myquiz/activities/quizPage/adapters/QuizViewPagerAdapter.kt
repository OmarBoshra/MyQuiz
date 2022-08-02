package com.example.myquiz.activities.quizPage.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myquiz.activities.quizPage.quizfragment

class QuizViewPagerAdapter (var numberOfFragments : Int,
                            activity: AppCompatActivity):FragmentStateAdapter(activity) {

    var currentposition = 0

    override fun getItemCount(): Int {
       return numberOfFragments
    }

    override fun createFragment(position: Int): Fragment {
        currentposition = position
        return quizfragment()
    }



}