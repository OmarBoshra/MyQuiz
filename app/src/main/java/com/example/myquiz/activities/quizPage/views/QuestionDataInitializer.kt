package com.example.myquiz.activities.quizPage.views


import com.example.myquiz.models.*

class QuestionDataInitializer(
    questionslistHashmap: LinkedHashMap<String, Question>,
    isNextQuestion: Boolean,
    var quizPageUIData: QuizPageUIData,
    answerResult: Boolean?
) {
    /**
     * Returns the information needed for the question
     * @param  questionslistHashmap
     * the list of questions from the viewmodel in hashmap form in order to prevent repetition of data and make data update possible.
     * @param  isFromViewModel
     *  weather the viewmodel called it or the ListListener , if from listListener then it will update the index else it will just update its data
     * @return * quizPageUIData
     */

    private fun setQuizPageUIData(
        currentQuestionIndex: Int,
        totalScore: Int,
        answersHashMap: HashMap<String, String>,
        correctAnswer: String,
        score: Int,
        question: String,
        numberOfQuestions: Int,
        url: String?
    ) {

        quizPageUIData.question = question
        quizPageUIData.currentQuestionIndex = currentQuestionIndex
        quizPageUIData.score = score
        quizPageUIData.totalScore = totalScore
        quizPageUIData.answersHashMap = answersHashMap
        quizPageUIData.correctAnswer = correctAnswer
        quizPageUIData.numberOfQuestions = numberOfQuestions
        quizPageUIData.questionImageUrl = url

    }

    init {

        //returns
        val islastQuestion: Boolean
        val answersHashMap: HashMap<String, String>
        val score: Int
        val question: String
        val correctAnswer: String
        val numberOfQuestions: Int
        var url: String? = null

        // gets and returns for itself
        var currentQuestionIndex = quizPageUIData.currentQuestionIndex
        var totalScore = quizPageUIData.totalScore

        // gets
        val questionslist = ArrayList(questionslistHashmap.values)

        if (questionslist.size > 0) {

            // next question counter
            if (!isNextQuestion)
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
                numberOfQuestions = questionslistHashmap.size

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
                    url = it
                }
                /*
                Set All The Data To Be Returned
                */
                setQuizPageUIData(
                    currentQuestionIndex,
                    totalScore,
                    answersHashMap,
                    correctAnswer,
                    score,
                    question,
                    numberOfQuestions,
                    url
                )
            }
            // if the user is at the last question
            if (currentQuestionIndex == questionslist.size) {

                islastQuestion = true
                quizPageUIData.islastQuestion = islastQuestion
            }
        }
    }
}