package com.example.myquiz.activities.quizPage

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myquiz.activities.MainActivity
import com.example.myquiz.activities.quizPage.adapters.RecyclerViewAdapter
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.models.*


class QuizPage : AppCompatActivity() {
    /**
     * Binding for all the activities views
     */
    private lateinit var binding: QuizPageBinding
    /**
     * Manipulated by method questionRenderer()
     */
    private lateinit var dialogProgress: Check24ProgressBar
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    /**
     * View model data
     */
    private lateinit var quizPageViewModel: QuizPageViewModel
    private lateinit var quizPageUIData: QuizPageUIData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize the UI binding
        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initiate progress dialogue
        dialogProgress = Check24ProgressBar(this@QuizPage)
        dialogProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress.show()

        initViewModel()


    }

    private fun initViewModel() {
        quizPageViewModel = ViewModelProvider(this)[QuizPageViewModel::class.java]
        quizPageViewModel.liveDataForUI.observe(this) {
            if (it == null) {
                Toast.makeText(this@QuizPage, "error in getting data", Toast.LENGTH_SHORT)
                    .show()
            } else {
                quizPageUIData = it
                iniRecyclerView()
                questionDataManager()
            }
        }
        quizPageViewModel.makeAPiCall()
    }

    private fun iniRecyclerView() {
        // initilaize recycler view
        recyclerViewAdapter = RecyclerViewAdapter()
        binding.answersRecyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.setOnItemClickListener2({
            // disable the timer
            quizPageUIData.questionTimer.removeCallbacksAndMessages(null)
            quizPageViewModel.onItemClick(recyclerViewAdapter.geItemPosition())

        })

/*        recyclerViewAdapter.setOnItemClickListener(
//            ListListener(
//                quizPageViewModel,
//                quizPageUIData.answersHashMap,
//                quizPageUIData.correctAnswer,
//                quizPageUIData.questionTimer
//            )
        )*/
    }

     fun questionDataManager() {
        // checking last question and updating the quiz data
        val islastQuestion = quizPageUIData.islastQuestion
        if (islastQuestion) {

            val navigtionIntent = Intent(this@QuizPage, MainActivity::class.java)
            // reset the model
            quizPageUIData = QuizPageUIData()
            navigtionIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            navigtionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(navigtionIntent)
        } else {

            if (quizPageUIData.numberOfQuestions> 0) {
                val questionScore = quizPageUIData.score
                val totalQuestions = quizPageUIData.numberOfQuestions
                val question = quizPageUIData.question
                renderQuestion(question, questionScore, totalQuestions, quizPageUIData)
            }
        }
    }

    private fun renderQuestion(
        question: String?,
        questionScore: Int,
        totalQuestions: Int,
        quizPageUIData: QuizPageUIData
    ) {
        //initialize progress indicator
        binding.progresIndicator.max = quizPageUIData.numberOfQuestions

        // set the question image
        Glide.with(binding.questionImage)
            .load(quizPageUIData.questionImageUrl)
            .apply(RequestOptions.centerCropTransform())
            .into( binding.questionImage)

        /*
         Set The questionIndicator
         */
        val questionIndicatorText: StringBuilder = StringBuilder()
        questionIndicatorText.append("Frage ")
        questionIndicatorText.append(quizPageUIData.currentQuestionIndex + 1)
        questionIndicatorText.append("/")
        questionIndicatorText.append(totalQuestions)
        questionIndicatorText.append(" - Aktuelle Punktzahl: ")
        questionIndicatorText.append(quizPageUIData.totalScore)
        binding.questionIndicator.text = questionIndicatorText

        // set the progressIndicator
        binding.progresIndicator.progress = quizPageUIData.currentQuestionIndex + 1

        // initialize the question score
        val questionScoreText: StringBuilder = StringBuilder()
        questionScoreText.append(questionScore)
        questionScoreText.append(" Punkte")
        binding.numberOfPoints.text = questionScoreText

        //initialize the question
        binding.question.text = question

       /*
       Update List of Answers
       */
        val answersList = quizPageUIData.answersHashMap.values
        val recyclerList = ArrayList<RecyclerData>()
        answersList.forEach {
            recyclerList.add(RecyclerData(it))
        }
        recyclerViewAdapter.setUpdatedData(recyclerList)

        dialogProgress.dismiss()
    }

}

