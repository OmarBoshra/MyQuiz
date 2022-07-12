package com.example.myquiz.interfaces


import com.example.myquiz.models.QuestionsAndAnswers

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("quiz.json")
    fun getdata(): Call<QuestionsAndAnswers>
}