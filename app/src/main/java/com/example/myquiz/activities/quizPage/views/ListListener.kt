package com.example.myquiz.activities.quizPage.views

import android.os.Handler
import android.view.ViewGroup
import com.example.myquiz.activities.quizPage.adapters.RecyclerViewAdapter
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.models.QuizPageViewModel

class ListListener(
    val context: QuizPageViewModel,
    val answersHashMap: HashMap<String,String>,
    val correctAnswer: String,
    val questionTimer: Handler,
) {

    private var answerResult: Boolean? = null

    fun onItemClick(
        myViewHolder: RecyclerViewAdapter.MyViewHolder,
        viewGroup: ViewGroup,
    ) {
            // to get the correct answer index
            var correctAnswerIndex = 0

             val answersList = answersHashMap.values

                answersList.forEachIndexed { index, element ->
                    if (element == answersHashMap[correctAnswer])
                        correctAnswerIndex = index
                    return@forEachIndexed
                }

            if (myViewHolder.buttonView.text == answersHashMap[correctAnswer]) {

                // todo notify viewmodel
                answerResult = true

            } else {
               // todo notify viewmodel

                answerResult = false
            }

            viewGroup.isEnabled = false


            // todo call handler after

    }

    fun getAnswerResult (): Boolean? {

        return answerResult
    }

//    fun onTouch(button: View) {
//        button.background = AppCompatResources.getDrawable(context, R.drawable.button_selected)
//
//    }
}



