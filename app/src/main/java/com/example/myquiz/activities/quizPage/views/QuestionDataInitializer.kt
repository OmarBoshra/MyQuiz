package com.example.myquiz.activities.quizPage.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import com.example.myquiz.activities.quizPage.HighscoreSaver
import com.example.myquiz.activities.quizPage.QuizPage
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.interfaces.QuizUIListener

import com.example.myquiz.models.Question
import com.example.myquiz.models.QuizPageData
import java.net.URL
import java.util.HashMap
import java.util.concurrent.Executors


class QuestionDataInitializer(
    private var QuizDataInterface: QuizUIListener,
    context: QuizPage,
    isFromViewModel:Boolean ,
    private var quizPageData: QuizPageData,
    dialogProgress: Check24ProgressBar
) {


    private fun returnsSetter(
        currentQuestionIndex: Int,
        totalScore: Int,
        answersHashMap: HashMap<String, String>,
        correctAnswer: String,
        answersList: ArrayList<String>
    ) {

        quizPageData.currentQuestionIndex = currentQuestionIndex
        quizPageData.totalScore = totalScore

        quizPageData.answersHashMap = answersHashMap
        quizPageData.correctAnswer = correctAnswer
        quizPageData.answersList = answersList




    }

    private fun questionData() {


        QuizDataInterface.callQuestionRenderer()


    }


    init {


        //returns
        val islastQuestion: Boolean
        val answersHashMap: HashMap<String, String>
        val correctAnswer: String
        val answersList: ArrayList<String>

        // gets and returns for itself
        var currentQuestionIndex = quizPageData.currentQuestionIndex
        var totalScore = quizPageData.totalScore
        val questionTimer = quizPageData.questionTimer

        // gets from listListener
        val adapter = quizPageData.adapter
        val answerResult = quizPageData.answerResult

        // gets
        val questionslist = ArrayList(context.questionslistHashmap.values)


        questionTimer.removeCallbacksAndMessages(null)
        if (questionslist.size>0) {

            // next question counter
            if(isFromViewModel == false)
              currentQuestionIndex++


        // check if the next question is still not the last question
        if (currentQuestionIndex < questionslist.size) {

//         quizPageData.answersList = ArrayList()
            // update progress indicator

            // show the new answers for the next question


            answersHashMap = questionslist[currentQuestionIndex].answers!!

            answersList = ArrayList(answersHashMap.values.toList())


            answersList.shuffle()


            adapter!!.addItems(answersList)
            adapter.notifyDataSetChanged()


            // change the header
            if (answerResult == true)
                totalScore += (
                        questionslist[currentQuestionIndex - 1].score!!
                        )

            correctAnswer = questionslist[currentQuestionIndex].correctAnswer!!

            questionslist[currentQuestionIndex].questionImageUrl?.let {

                if (it == ("null")) {
                    quizPageData.questionImageBitMap = null
                    dialogProgress.dismiss()
                    returnsSetter(
                        currentQuestionIndex,
                        totalScore,
                        answersHashMap,
                        correctAnswer,
                        answersList
                    )
                    questionData()

                } else {

                    downloadImageFromInternet(
                        it,
                        dialogProgress
                    ) {
                        dialogProgress.dismiss()
                        returnsSetter(
                            currentQuestionIndex,
                            totalScore,
                            answersHashMap,
                            correctAnswer,
                            answersList
                        )
                        questionData()

                    }

                }

            } ?: kotlin.run {
                quizPageData.questionImageBitMap = null
                dialogProgress.dismiss()
                returnsSetter(
                    currentQuestionIndex,
                    totalScore,
                    answersHashMap,
                    correctAnswer,
                    answersList
                )
                questionData()

            }


// if the user is at the last question
        }


        if (currentQuestionIndex == questionslist.size) {


            HighscoreSaver(context, totalScore)
            dialogProgress.dismiss()


                islastQuestion = true
                quizPageData.islastQuestion = islastQuestion



            questionData()
        } else {


            val questionTimer = Handler(Looper.getMainLooper())
            questionTimer.postDelayed({

                quizPageData.answerResult = false

                QuestionDataInitializer(
                    QuizDataInterface, context,
                    false,quizPageData, dialogProgress
                )

            }, 10000)

            quizPageData.questionTimer = questionTimer


        }
        }


    }


    private fun downloadImageFromInternet(
        url: String,
        dialogProgress: Check24ProgressBar,
        callBack: () -> Unit
    ) {

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())


        var imageBitMap: Bitmap?
        executor.execute {

            // Image URL

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = URL(url).openStream()
                imageBitMap = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {

                    dialogProgress.dismiss()
                    quizPageData.questionImageBitMap = imageBitMap

                    callBack.invoke()


                }


            }


            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}