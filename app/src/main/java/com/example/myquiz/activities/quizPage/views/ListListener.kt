package com.example.myquiz.activities.quizPage.views

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.myquiz.R
import com.example.myquiz.activities.quizPage.QuizPage
import com.example.myquiz.activities.quizPage.adapters.RecyclerViewAdapter
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.interfaces.QuizUIListener
import com.example.myquiz.models.QuizPageData

class ListListener(
    private var QuizDataInterface: QuizUIListener,
    private var quizPageData: QuizPageData,
    val context: QuizPage,
    private val dialogProgress: Check24ProgressBar
) {

    fun onItemClick(
        pressedView: View?,
        myViewHolder: RecyclerViewAdapter.MyViewHolder,
        viewGroup: ViewGroup,
    ) {

//        val item = parent?.getItemAtPosition(position)
        if (pressedView!!.isPressed) {

            // gets
            val answersList = quizPageData.answersList
            val answersHashMap = quizPageData.answersHashMap
            val correctAnswer = quizPageData.correctAnswer
            val questionTimer = quizPageData.questionTimer

            // disable the timer

            questionTimer.removeCallbacksAndMessages(null)


            val answerResult: Boolean

            // to get the correct answer index
            var correctAnswerIndex = 0
            answersList.forEachIndexed { index, element ->

                if (element.answer == answersHashMap!![correctAnswer])
                    correctAnswerIndex = index
                return@forEachIndexed
            }

            if (myViewHolder.buttonView.text == answersHashMap!![correctAnswer]) {
                Toast.makeText(
                    context.applicationContext,
                    "Correct answer",
                    Toast.LENGTH_SHORT
                ).show()
                answerResult = true
                myViewHolder.buttonView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
            } else {
                Toast.makeText(
                    context.applicationContext,
                    "InCorrect answer",
                    Toast.LENGTH_SHORT
                ).show()
                myViewHolder.buttonView.setBackgroundColor(ContextCompat.getColor(context, R.color.red))

                answersList.let {
                    viewGroup.getChildAt(correctAnswerIndex)
                        .setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green
                            )
                        )
                }

                answerResult = false

            }

            viewGroup.isEnabled = false

            dialogProgress.setCancelable(false)
            dialogProgress.show()

            Handler(Looper.getMainLooper()).postDelayed({

                viewGroup.isEnabled = true

                myViewHolder.buttonView.background = AppCompatResources.getDrawable(context, R.drawable.button_default)



                viewGroup.getChildAt(correctAnswerIndex).background = AppCompatResources.getDrawable(context, R.drawable.button_default)


                QuestionDataInitializer(
                    QuizDataInterface, context,
                    false, quizPageData, dialogProgress
                )

            }, 2000)

            quizPageData.answerResult = answerResult


        }


    }

//    fun onTouch(button: View) {
//        button.background = AppCompatResources.getDrawable(context, R.drawable.button_selected)
//
//    }
}



