package com.example.myquiz.activities.quizPage.views



import android.os.Handler
import android.os.Looper
import com.example.myquiz.interfaces.QuizUIListener
import com.example.myquiz.models.*

class QuestionDataInitializer(
    private var questionslistHashmap: LinkedHashMap<String, Question>,
    isFromViewModel: Boolean,
    var answerResult: Boolean?
) {

    private val quizPageUIData = QuizPageUIData()

    /**
     * Returns the information needed for the question
     * @param  questionslistHashmap
     * the list of questions from the viewmodel in hashmap form in order to prevent repetition of data and make data update possible.
     * @param  isFromViewModel
     *  weather the viewmodel called it or the ListListener , if from listListener then it will update the index else it will just update its data
     * @return * quizPageUIData
     */

    private fun returnsSetter(
        currentQuestionIndex: Int,
        totalScore: Int,
        answersHashMap: HashMap<String, String>,
        correctAnswer: String,
        score: Int,
        question: String,
        numberOfQuestions:Int
    ) {

        quizPageUIData.question = question
        quizPageUIData.currentQuestionIndex = currentQuestionIndex
        quizPageUIData.score = score
        quizPageUIData.totalScore = totalScore
        quizPageUIData.answersHashMap = answersHashMap
        quizPageUIData.correctAnswer = correctAnswer
        quizPageUIData.numberOfQuestions = numberOfQuestions


    }

    init {

        //returns
        val islastQuestion: Boolean
        val answersHashMap: HashMap<String, String>
        val score: Int
        val question: String
        val correctAnswer: String
        val numberOfQuestions: Int

        // gets and returns for itself
        var currentQuestionIndex = quizPageUIData.currentQuestionIndex
        var totalScore = quizPageUIData.totalScore
        val questionTimer = quizPageUIData.questionTimer

        // gets
        val questionslist = ArrayList(questionslistHashmap.values)

        questionTimer.removeCallbacksAndMessages(null)
        if (questionslist.size > 0) {

            // next question counter
            if (!isFromViewModel)
                currentQuestionIndex++


            // check if the next question is still not the last question
            if (currentQuestionIndex < questionslist.size) {


                // show the next Question
                question = questionslist[currentQuestionIndex].question!!

                // show the new answers for the next question
                answersHashMap = questionslist[currentQuestionIndex].answers!!

                // show the next Questions score
                score = questionslist[currentQuestionIndex].score!!

                // show the next Questions score
                numberOfQuestions  = questionslistHashmap.size


// todo update via view model
//            adapter!!.addItems(answersList)
//            adapter.notifyDataSetChanged()

                // change the header
                if (answerResult == true)
                    totalScore += (
                            questionslist[currentQuestionIndex - 1].score!!
                            )

                correctAnswer = questionslist[currentQuestionIndex].correctAnswer!!

    /**
     * To get the imageUrl and set it in QuizPageActivity using Glide
     */
                questionslist[currentQuestionIndex].questionImageUrl?.let {
                        quizPageUIData.questionImageUrl = it
                }
                /*
                Set All The Data To Be Returned
                */
                returnsSetter(
                    currentQuestionIndex,
                    totalScore,
                    answersHashMap,
                    correctAnswer,
                    score,
                    question,
                    numberOfQuestions
                )
            }
            // if the user is at the last question
            if (currentQuestionIndex == questionslist.size) {

                // todo make in the activity
//                HighscoreSaver(context, totalScore)
                islastQuestion = true
                quizPageUIData.islastQuestion = islastQuestion
            } else {
//                val questionTimer = Handler(Looper.getMainLooper())
//                questionTimer.postDelayed({
//                 answerResult = false
//
//                    QuestionDataInitializer(
//                        questionslistHashmap,
//                        false ,false
//                    )
//                }, 10000)
//                listListenerData.questionTimer = questionTimer
            }
        }
    }

    fun getUIData ():QuizPageUIData{
        return quizPageUIData
    }

}