package com.example.myquiz.models


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


) {

    fun letterchecker(letter: String): String? {

        return when (letter) {
            "A"-> A
            "B" -> B
            "C" -> C
            "D" -> D
            else -> { // Note the block
                null
            }
        }

    }


}


//fun toString(): String? {
//    return "myVar: " + Answers.toString() + " | myOtherVar: " + myOtherVar
//}