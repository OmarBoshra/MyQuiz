package com.example.myquiz.models


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myquiz.QuizApplication
import com.example.myquiz.interfaces.ApiInterface
import kotlinx.coroutines.launch
import javax.inject.Inject


class QuizPageViewModel(application: Application) : AndroidViewModel(application) {
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
                val responseBody = quizDataResponse.body()
                if (responseBody != null) {
                    responseBody.questions.forEach {

                        liveDataList.value = it
                    }
                } else {
                    liveDataList.postValue(null)
                }
            }
        }
    }
}