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
     * ## QuizPageViewModel
     * Manages __communication__  between Activity/itsfragments and Models within API or other data models , thus separating between the business logic and the UI
     * @param  [mService] Service of the API injected within the directed acyclic graph
     *
     * @param  [liveDataForUI] Live data to be sent from the viewmodel
     *
     * @param  [questionslistHashmap] List of ordered questions which are sent to the QuestionDataInitializer
     *
     * @param  [quizPageData] the data needed to update UI
     * @return  - liveDataForUI
     *  - answerResult
     */
    @Inject
    lateinit var mService: ApiInterface

    /**
     * Live data to be sent from the viewmodel
     */
    var liveDataForUI = MutableLiveData<QuizPageUIData?>()
    /**
     * List of ordered questions which are sent to the QuestionDataInitializer
     */
    private var questionslistHashmap: LinkedHashMap<String, Question> = LinkedHashMap()

    /**
     * Date returned to update the UI and also gets manipulated by the UI back to the viewmodel
     */
    private var quizPageUIData = QuizPageUIData()

    init {
        /**
         * initialize the application class and inject to its dagger2 component the viewmodel
         */
        (application as QuizApplication).getRetroComponent().inject(this)
    }

    /**
     * ## makeAPiCall
     * To __fetch__ data from [url](https://app.check24.de/vg2-quiz/quiz.json) and send it to the [QuizPageAactivity][com.example.myquiz.activities.quizPage.QuizPage]
     * via [liveDataForUI]
     */
    fun makeAPiCall() {
        viewModelScope.launch {
            val quizDataResponse = mService.getQuizData()
            if (quizDataResponse.isSuccessful) {
                val responseBody = quizDataResponse.body()
                if (responseBody != null) {
                    responseBody.questions.forEach {
                        it.question?.let { id ->
                            // here the question was considered to be the id but of course in reality this would not be the case
                            questionslistHashmap[id] = it

                            quizPageUIData = QuestionDataInitializer(
                                questionslistHashmap,
                                false, quizPageUIData, null
                            ).quizPageUIData

                            liveDataForUI.value = quizPageUIData

                        }
                    }
                } else {
                    liveDataForUI.value = null
                }
            }
        }
    }

    /**
     * ## onItemClick
     * **Updates** the Business logic of the Click Listner then notifies the activity of the result
     */
    fun onItemClick(
        answer: String
    ): MutableList<Any> {
        // returns
        var answerResult: Boolean? = null
        var correctanswer = ""
        // to get the correct answer and index
        var correctAnswerIndex = 0
        val answersList = quizPageUIData.answersHashMap.values
        // to find both the index and the correct answer itself
        answersList.forEachIndexed { index, element ->
            if (element == quizPageUIData.answersHashMap[quizPageUIData.correctAnswer]) {
                correctanswer = element
                correctAnswerIndex = index
            }
            return@forEachIndexed
        }

        answerResult = answer == correctanswer

        return mutableListOf(answerResult, correctAnswerIndex)
    }

    /**
     * ## toNextQuestion
     * for __rendering__ the next question
     * - calls the [QuestionDataInitializer]
     * - sets the new [liveDataForUI]
     * @param answerResult
     * the result of the previous answer so that the [QuestionDataInitializer] can know if its going to add the previous answer to the total score or no
     * */
    fun toNextQuestion(answerResult: Boolean) {

        quizPageUIData = QuestionDataInitializer(
            questionslistHashmap,
            true, quizPageUIData, answerResult
        ).quizPageUIData

        liveDataForUI.value = quizPageUIData

    }

    // to update the index after going to the next fragment
    fun updateQuestionIndex(currentPostionofFragment: Int) {
       // to next fragement
        if(quizPageUIData.currentQuestionIndex  != currentPostionofFragment){
            quizPageUIData.currentQuestionIndex = currentPostionofFragment
            toNextQuestion(false)
        }

    }
}