package com.example.myquiz.activities.quizPage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myquiz.R
import com.example.myquiz.activities.MainActivity
import com.example.myquiz.activities.quizPage.adapters.RecyclerViewAdapter
import com.example.myquiz.customWidgets.Check24ProgressBar
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
    private lateinit var dialogProgress: Check24ProgressBar
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var quizPageViewModel: QuizPageViewModel
    private lateinit var quizPageUIData: QuizPageUIData
    private var questionTimer: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize the UI binding
        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initiate progress dialogue
        dialogProgress = Check24ProgressBar(this@QuizPage)
        dialogProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        initViewModel()
        iniRecyclerView()
    }

    /**
     * ## initViewModel
     * @since [liveDataForUI][com.example.myquiz.models.QuizPageViewModel.liveDataForUI] is observed
     * * initViewModel Observes the data that should come to the UI
     * * once the data arrives it reinitializes [iniRecyclerView] and [questionDataManager] with the new data
     * */
    private fun initViewModel() {
        quizPageViewModel = ViewModelProvider(this)[QuizPageViewModel::class.java]
        quizPageViewModel.liveDataForUI.observe(this) {
            if (it == null) {
                Toast.makeText(this@QuizPage, "error in getting data", Toast.LENGTH_SHORT)
                    .show()
            } else {
                quizPageUIData = it
                questionDataManager()
            }
        }

        quizPageViewModel.makeAPiCall()
    }

    /**
     * ## iniRecyclerView
     * * initializes the recyclerview
     * * initializes the recyclerview's listener which gets its data from the [QuizPageViewModel] [com.example.myquiz.models.QuizPageViewModel]
     * */
    private fun iniRecyclerView() {
        // initialize recycler view and set both its adapter and listener
        recyclerViewAdapter = RecyclerViewAdapter()
        binding.answersRecyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.setOnItemClickListener2 { answer, postion ->
            // disable the timer
            if (questionTimer != null) {
                questionTimer!!.removeCallbacksAndMessages(null)
                questionTimer = null
            }


            val answerData = quizPageViewModel.onItemClick(answer)
            val answerResult = answerData[0]
            val correctAnswerIndex = answerData[1]

            val selectedView =
                binding.answersRecyclerView.findViewHolderForAdapterPosition(postion)?.itemView
            val correctAnswerView = binding.answersRecyclerView.findViewHolderForAdapterPosition(
                correctAnswerIndex as Int
            )?.itemView

            if (answerResult as Boolean) {
                // if correct make sure row is green
                selectedView?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.green
                    )
                )
            } else {
                // make sure to make it red and make the correct answer green
                selectedView?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.red
                    )
                )
                correctAnswerView?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.green
                    )
                )

            }
            // reinitialize the dialog
            dialogProgress.show()
            Handler(Looper.getMainLooper()).postDelayed({

                selectedView?.background =
                    AppCompatResources.getDrawable(this, R.drawable.button_default)

                correctAnswerView?.background =
                    AppCompatResources.getDrawable(this, R.drawable.button_default)

                quizPageViewModel.toNextQuestion(answerResult)
            }, 2000)
        }
    }

    /**
     * ## questionDataManager
     * * makes sure to save the highestScore using [HighscoreSaver] and navigate to [MainActivity][com.example.myquiz.activities.MainActivity] if its the last question
     * * else it calls [renderQuestion]
     * */
    private fun questionDataManager() {
        // checking last question and updating the quiz data
        val islastQuestion = quizPageUIData.islastQuestion
        if (islastQuestion) {
            // save the highestscore
            HighscoreSaver(this, quizPageUIData.totalScore)

            val navigtionIntent = Intent(this@QuizPage, MainActivity::class.java)
            // reset the model
            quizPageUIData = QuizPageUIData()
            navigtionIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            navigtionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(navigtionIntent)
        } else {

            if (quizPageUIData.numberOfQuestions > 0) {
                val questionScore = quizPageUIData.score
                val totalQuestions = quizPageUIData.numberOfQuestions
                val question = quizPageUIData.question
                renderQuestion(question, questionScore, totalQuestions, quizPageUIData)
            }
        }
    }

    /**
     * ### renderQuestion
     * * makes sure to render all the data that where recived from the viwmodel through the [quizPageUIData] object
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun renderQuestion(
        question: String?,
        questionScore: Int,
        totalQuestions: Int,
        quizPageUIData: QuizPageUIData
    ) {
        //initialize progress indicator
        binding.progresIndicator.max = quizPageUIData.numberOfQuestions

        // set the question image
        binding.questionImage.setImageBitmap(null)
        if (quizPageUIData.questionImageUrl != null) {

            Glide.with(binding.questionImage)
                .load(quizPageUIData.questionImageUrl)
                .apply(RequestOptions().override(900, 500))
                .into(binding.questionImage)
        }

        /*
         Set The questionIndicator
         */
        val questionIndicatorText = resources.getString(
            R.string.QuestionIndicator,
            quizPageUIData.currentQuestionIndex + 1,
            totalQuestions,
            quizPageUIData.totalScore
        )
        binding.questionIndicator.text = questionIndicatorText

        // set the progressIndicator
        binding.progresIndicator.progress = quizPageUIData.currentQuestionIndex + 1

        // initialize the question score

        val questionScoreText = resources.getString(R.string.Punkte, questionScore)
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
        recyclerViewAdapter.notifyDataSetChanged()

        dialogProgress.dismiss()

        if (questionTimer == null) {
            questionTimer = Handler(Looper.getMainLooper())
            questionTimer!!.postDelayed({
                // cancel the timer in order to restart it
                questionTimer!!.removeCallbacksAndMessages(null)
                questionTimer = null
                // reinitialize the dialog
                dialogProgress.show()
                // call the model again assuming that the answer is false
                quizPageViewModel.toNextQuestion(false)
            }, 10000)
        }

    }

}

