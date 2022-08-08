package com.example.myquiz.activities.quizPage

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.myquiz.activities.quizPage.adapters.QuizViewPagerAdapter
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.models.*


class QuizPage : AppCompatActivity() {
    /**
     * # QuizPage
     * The Page showing the quizQuestions
     * @param binding Binding for all the activities views
     * Manipulated By [renderQuestion]
     * @param dialogProgress progress dialog awaiting data fromviewModel
     * @param recyclerViewAdapter progress dialog awaiting data fromviewModel
     * View model data
     * @param quizPageViewModel the viewmodel object to be instantiated
     * @param quizPageUIData data coming from viewmodel
     * @param questionTimer handle the timing between every question as well as after answering
     * */

    private lateinit var binding: QuizPageBinding
    lateinit var adapter: QuizViewPagerAdapter
    lateinit var quizPageViewModel: QuizPageViewModel
    lateinit var quizPageUIData : QuizPageUIData

    private val myViewModel by viewModels<FragmentsUpdateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize the UI binding
        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initViewFragmentsModel()
    }

    private fun initViewFragmentsModel() {

        myViewModel.updateQuestionIndex.observe(this, { currentPostionofFragment ->

            quizPageViewModel.updateQuestionIndex(currentPostionofFragment)

        })
        myViewModel.onItemClick.observe(this, { answer ->

            myViewModel.setCorrectAnswer(quizPageViewModel.onItemClick(answer))
        })
        myViewModel.toNextQuestion.observe(this, { answerResult ->

            quizPageViewModel.toNextQuestion(answerResult)
        })

    }

    /**
     * ## initViewModel
     * @since [liveDataForUI][com.example.myquiz.models.QuizPageViewModel.liveDataForUI] is observed
     * * initViewModel Observes the data that should come to the UI
     * * once the data arrives it reinitializes [iniRecyclerView] and [questionDataManager] with the new data
     * */
    private fun initViewModel() {

        adapter = QuizViewPagerAdapter(1, this@QuizPage)
        binding.quizviewpager.adapter = adapter

        quizPageViewModel = ViewModelProvider(this@QuizPage)[QuizPageViewModel::class.java]

        quizPageViewModel.liveDataForUI.observe(this@QuizPage) {
            if (it == null) {
                Toast.makeText(this@QuizPage, "error in getting data", Toast.LENGTH_SHORT)
                    .show()
            } else {
                quizPageUIData = it

//                val uiHandler = Handler(Looper.getMainLooper())
//                uiHandler.post(Runnable {
//
//                    adapter!!.numberOfFragments = it.numberOfQuestions
//                    adapter!!.notifyDataSetChanged()
//                    adapter!!.currentposition = quizPageUIData!!.currentQuestionIndex
//                    binding.quizviewpager.currentItem = quizPageUIData!!.currentQuestionIndex
//
//                })




                myViewModel.setUIData(quizPageUIData)
            }
        }
        quizPageViewModel.makeAPiCall()
    }

    class FragmentsUpdateViewModel(application: Application): AndroidViewModel(application) {
        var fragmentUIData = MutableLiveData<QuizPageUIData>() //typically private and exposed through method
        var answerData = MutableLiveData<MutableList<Any>>() //typically private and exposed through method
        val updateQuestionIndex = MutableLiveData<Int>() //typically private and exposed through method
        val onItemClick = MutableLiveData<String>() //typically private and exposed through method
        val toNextQuestion = MutableLiveData<Boolean>() //typically private and exposed through method

        fun setUIData(value: QuizPageUIData) {
            fragmentUIData.value = value //triggers observers
        }

        fun setCorrectAnswer(correctAnswer: MutableList<Any>) {
            answerData.value = correctAnswer //triggers observers
        }

        fun updateQuestionIndex(currentPostionofFragment: Int) {
            updateQuestionIndex.value = currentPostionofFragment //triggers observers

        }

        fun onItemClick(answer: String) {
            onItemClick.value = answer //triggers observers

        }

        fun toNextQuestion(answerResult: Boolean) {
            toNextQuestion.value = answerResult //triggers observers

        }

    }

}

