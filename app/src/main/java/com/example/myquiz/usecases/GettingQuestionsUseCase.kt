package com.example.myquiz
import com.example.myquiz.models.QuestionsAndAnswers


import com.example.myquiz.activities.quizPage.views.QuestionDataInitializer
import com.example.myquiz.interfaces.ApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Inject


class GettingQuestionsUseCase @Inject constructor(val mService:ApiInterface) {

    suspend fun getQuestionsData() :QuestionsAndAnswers? {

        val quizDataResponse = mService.getQuizData()
        if (quizDataResponse.isSuccessful) {
            val responseBody = quizDataResponse.body()
            return responseBody
        } else {
            return null
        }
    }
}
