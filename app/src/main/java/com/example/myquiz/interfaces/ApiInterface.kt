package com.example.myquiz.interfaces


import com.example.myquiz.models.QuestionsAndAnswers

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("quiz.json")
    fun getdata(): Call<QuestionsAndAnswers>
//    fun getdataFromAPI(@Query("q")query:String): Call<QuestionsAndAnswers>
}