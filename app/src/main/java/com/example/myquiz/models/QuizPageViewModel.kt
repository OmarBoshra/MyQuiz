package com.example.myquiz.models


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myquiz.QuizApplication
import com.example.myquiz.interfaces.ApiInterface
import kotlinx.coroutines.launch
import javax.inject.Inject


class QuizPageViewModel (application: Application): AndroidViewModel(application) {
    @Inject
    lateinit var mService: ApiInterface


    var liveDataList = MutableLiveData<Question?>()

    init {
        // initialize the application class

        (application as QuizApplication).getRetroComponent().inject(this)

        liveDataList = MutableLiveData()
    }


    fun makeAPiCall() {
        viewModelScope.launch {
            val quizDataResponse = mService.getQuizData()

            if (quizDataResponse.isSuccessful) {

                var responseBody = quizDataResponse.body()


//                val StringBuilder = StringBuilder()
                if (responseBody != null) {
                    responseBody.questions.forEach {
                        liveDataList.value = it


                    }


//                val questionsAndAnswers: QuestionsAndAnswers = gson.fromJson(data, listType)
//                questionslist = questionsAndAnswers.questions
//
//                liveDataList.postValue()


                } else {
                    liveDataList.postValue(null)
                }

            }


        }

    }
}