package com.example.myquiz.models


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myquiz.QuizApplication
import com.example.myquiz.activities.quizPage.views.QuestionDataInitializer
import com.example.myquiz.interfaces.ApiInterface
import kotlinx.coroutines.launch
import javax.inject.Inject


class QuizPageViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Service of the API injected within the directed acyclic graph
     */
    @Inject
    lateinit var mService: ApiInterface
    /**
     * Live data to be sent from the viewmodel
     */
    var liveDataForUI= MutableLiveData<QuizPageUIData?>()
    /**
     * List of ordered questions which are sent to the QuestionDataInitializer
     */
    var questionslistHashmap: LinkedHashMap<String, Question> = LinkedHashMap()
    /**
     * Object to retrive the question data
     */
    var questionInitializerObj: QuestionDataInitializer? =null

    init {
        /**
         * initialize the application class and inject to its dagger2 component the viewmodel
         */
        (application as QuizApplication).getRetroComponent().inject(this)

    }
    /**
     * To get data from json
     */
    fun makeAPiCall() {
        viewModelScope.launch{

            var quizPageData: QuizPageUIData? = null

            val quizDataResponse = mService.getQuizData()
            if (quizDataResponse.isSuccessful) {
                val responseBody = quizDataResponse.body()
                if (responseBody != null) {
                    responseBody.questions.forEach {
                        it.question?.let { id ->
                            // here the question was considered to be the id but of course in reality this would not be the case
                            questionslistHashmap[id] = it

                            quizPageData = QuestionDataInitializer(
                                questionslistHashmap,
                                true,null).getUIData()

                            liveDataForUI.value = quizPageData

                        }
                    }
                } else {
                    liveDataForUI.value = null
                }
            }
        }
    }

    fun onItemClick(geItemPosition: Int?) {

    }
}