package com.example.myquiz.models


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myquiz.QuizApplication
import com.example.myquiz.interfaces.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class QuizPageViewModel (application: QuizApplication):AndroidViewModel(application){
@Inject
lateinit var mService:ApiInterface

private lateinit var liveDataList: MutableLiveData<RecyclerData>

init {
    // initialize the application class

    (application).getRetroComponent().inject(this)

    liveDataList = MutableLiveData()
}

    fun getLiveDataObserver():MutableLiveData<RecyclerData>{
        return liveDataList


    }

    fun makeAPiCall() {
        val retrofitdata = mService.getdata()
        retrofitdata.enqueue(object : Callback<QuestionsAndAnswers?> {
            override fun onResponse(
                call: Call<QuestionsAndAnswers?>,
                response: Response<QuestionsAndAnswers?>
            ) {

                val responseBody = response.body()
//                liveDataList.postValue(responseBody)
//                val StringBuilder = StringBuilder()
                responseBody?.questions?.forEach {


                }
            }

            override fun onFailure(call: Call<QuestionsAndAnswers?>, t: Throwable) {

//                liveDataList.postValue(null)
            }
        })

    }

}