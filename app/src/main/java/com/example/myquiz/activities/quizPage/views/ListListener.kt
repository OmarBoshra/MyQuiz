package com.example.myquiz.activities.quizPage.views

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.myquiz.R
import com.example.myquiz.activities.quizPage.QuizPage
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.interfaces.QuizUIListener
import com.example.myquiz.models.QuizPageData

class ListListener(
    private var QuizDataInterface: QuizUIListener,
    private var quizPageData: QuizPageData,
    val context: QuizPage,
    private val dialogProgress: Check24ProgressBar
) :
    AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {

        val item = parent?.getItemAtPosition(position)
        if (parent!!.isPressed) {

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

                if(element.answer== answersHashMap!![correctAnswer])
                    correctAnswerIndex = index
                return
            }

            if (item.toString() == answersHashMap!![correctAnswer]) {
                Toast.makeText(
                    context.applicationContext,
                    "Correct answer",
                    Toast.LENGTH_SHORT
                ).show()
                answerResult = true
                view!!.setBackgroundColor(
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
                view!!.setBackgroundColor(ContextCompat.getColor(context, R.color.red))

                answersList.let {
                    parent.getChildAt(correctAnswerIndex)
                        .setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green
                            )
                        )
                }

                answerResult = false

            }


            parent.isEnabled = false


            dialogProgress.setCancelable(false)
            dialogProgress.show()


//
//                    val timerobj  =Timers()
//                    timerobj.questionWaitTimer()
//


            Handler(Looper.getMainLooper()).postDelayed({

                parent.isEnabled = true

                view.background =
                    AppCompatResources.getDrawable(context, R.drawable.button_default)


                parent.getChildAt(correctAnswerIndex).background =
                    AppCompatResources.getDrawable(context, R.drawable.button_default)


                QuestionDataInitializer(
                    QuizDataInterface, context,
                    false,quizPageData, dialogProgress
                )

            }, 2000)

            quizPageData.answerResult = answerResult


        }


    }

    fun onItemClick(parent: Int) {
        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show()
    }
}