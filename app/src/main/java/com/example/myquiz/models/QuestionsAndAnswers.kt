package com.example.myquiz.models


data class QuestionsAndAnswers(
    val questions: List<Question>,



)

data class Question(
    val answers: HashMap<String, String>? = null,
    val correctAnswer: String? = null,
    val question: String? = null,
    val questionImageUrl: String? = null,
    val score: Int? = null,
)




//fun toString(): String? {
//    return "myVar: " + Answers.toString() + " | myOtherVar: " + myOtherVar
//}