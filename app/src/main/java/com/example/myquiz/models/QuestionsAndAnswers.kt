package com.example.myquiz.models

//import com.beust.klaxon.*
data class QuestionsAndAnswers(
    val questions: List<Question>


)

data class Question(
    val answers: Answers? = null,
    val correctAnswer: String? = null,
    val question: String? = null,
    val questionImageUrl: String? = null,
    val score: Int? = null,
)

data class Answers(
    val A: String? = null,
    val B: String? = null,
    val C: String? = null,
    val D: String? = null
)

//fun toString(): String? {
//    return "myVar: " + Answers.toString() + " | myOtherVar: " + myOtherVar
//}