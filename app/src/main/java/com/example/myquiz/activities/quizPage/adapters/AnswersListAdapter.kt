package com.example.myquiz.activities.quizPage.adapters

import android.content.Context
import android.widget.ArrayAdapter

class AnswersListAdapter(
    context: Context,
    resource: Int,
    private var answerslist: ArrayList<String>
) :
    ArrayAdapter<String>(context, resource, answerslist) {


    fun addItems(answers: List<String>) {
        answerslist.clear()
        answerslist.addAll(answers)
    }


}