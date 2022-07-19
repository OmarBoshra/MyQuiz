package com.example.myquiz.activities.quizPage

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiz.activities.MainActivity
import com.example.myquiz.activities.quizPage.adapters.RecyclerViewAdapter
import com.example.myquiz.activities.quizPage.views.ListListener
import com.example.myquiz.activities.quizPage.views.QuestionDataInitializer
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.interfaces.QuizUIListener
import com.example.myquiz.models.Question
import com.example.myquiz.models.QuizPageData
import com.example.myquiz.models.QuizPageViewModel


class QuizPage : AppCompatActivity(), QuizUIListener {

    private lateinit var binding: QuizPageBinding

    var questionslistHashmap: LinkedHashMap<String, Question> = LinkedHashMap()

    private lateinit var dialogProgress: Check24ProgressBar

    private var quizPageData = QuizPageData()

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var quizPageViewModel: QuizPageViewModel

    private fun initViewModel() {
        quizPageViewModel = ViewModelProvider(this)[QuizPageViewModel::class.java]

        quizPageViewModel.liveDataList.observe(this) {

            if (it != null) {

                it.question?.let { id ->

                    // here the question was considered to be the id but ofcourse in reality this would not be the case
                    questionslistHashmap[id] = it

                    QuestionDataInitializer(
                        this@QuizPage, this@QuizPage,
                        true, quizPageData, dialogProgress
                    )

                }

            } else {
                Toast.makeText(this@QuizPage, "error in getting data", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        quizPageViewModel.makeAPiCall()
    }

    private fun iniRecyclerView() {

        // initilaize recycler view
        recyclerViewAdapter = RecyclerViewAdapter()
        binding.answersRecyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.setOnItemClickListener(
            ListListener(
                this@QuizPage,
                quizPageData,
                this@QuizPage,
                dialogProgress
            )
        )

        quizPageData.adapter = recyclerViewAdapter

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize the UI binding
        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initiate progress dialogue
        dialogProgress = Check24ProgressBar(this@QuizPage)
        dialogProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress.show()

        receiveQuizData()

        iniRecyclerView()
        initViewModel()

    }

    private fun receiveQuizData() {

        // first initialization of the questionsData
        QuestionDataInitializer(
            this@QuizPage, this@QuizPage,
            false, quizPageData, dialogProgress
        )
    }


    private fun questionRenderer(
        question: String?,
        questionScore: Int,
        totalQuestions: Int,
        quizPageData: QuizPageData
    ) {

        //initialize progress indicator
        binding.progresIndicator.max = questionslistHashmap.size
        // set the question image
        binding.questionImage.setImageBitmap(null)
        binding.questionImage.setImageBitmap(quizPageData.questionImageBitMap)
        // set the quizheader
        val questionIndicatorText: StringBuilder = StringBuilder()

        questionIndicatorText.append("Frage ")
        questionIndicatorText.append(quizPageData.currentQuestionIndex + 1)
        questionIndicatorText.append("/")
        questionIndicatorText.append(totalQuestions)
        questionIndicatorText.append(" - Aktuelle Punktzahl: ")
        questionIndicatorText.append(quizPageData.totalScore)

        binding.questionIndicator.text = questionIndicatorText

        // set the progressIndicator

        binding.progresIndicator.progress = quizPageData.currentQuestionIndex + 1

        // initialize the question score


        val questionScoreText: StringBuilder = StringBuilder()
        questionScoreText.append(questionScore)
        questionScoreText.append(" Punkte")
        binding.numberOfPoints.text = questionScoreText

        //initialize the question
        binding.question.text = question


    }


    override fun callQuestionRenderer() {


        // checking last question and updating the quiz data

        val islastQuestion = quizPageData.islastQuestion
        if (islastQuestion) {
            val navigtionIntent = Intent(this@QuizPage, MainActivity::class.java)
            // reset thr model
            quizPageData = QuizPageData()
            navigtionIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            navigtionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(navigtionIntent)
        } else {

            val questionslist = ArrayList(questionslistHashmap.values)
            if (questionslist.size > 0) {
                val questionScore = questionslist[quizPageData.currentQuestionIndex].score!!
                val totalQuestions = questionslist.size
                val question = questionslist[quizPageData.currentQuestionIndex].question
                questionRenderer(question, questionScore, totalQuestions, quizPageData)
            }

        }


    }


    // todo modulate the QuestionDatainitializer further and maybe add interface between it and quizpage activity
    // todo add the timers without sending them extra data
    // todo smoke test
    // todo add workmanager API
    // todo re inspect the code


}

