package com.example.myquiz.Activities.QuizPage

import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myquiz.Activities.QuizPage.Adapters.AnswersListAdapter
import com.example.myquiz.Activities.QuizPage.Views.ListInitializer
import com.example.myquiz.Activities.QuizPage.Views.QuestionDataInitializer
import com.example.myquiz.R
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.models.Answers
import com.example.myquiz.models.Question
import com.example.myquiz.models.QuestionsAndAnswers
import com.example.myquiz.custom_widgets.Check24ProgressBar
import com.example.myquiz.utilities.Timers
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

import kotlin.collections.ArrayList


class QuizPage : AppCompatActivity() {

    private lateinit var binding: QuizPageBinding

    // private lateinit var widgetBinding: widget


    private lateinit var dialogProgress: Check24ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // initialize the UI binding
        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)



        dialogProgress = Check24ProgressBar(this@QuizPage)
        dialogProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress.show()

        // initiate receiving the quiz data
        recivedQuizData()


    }

    fun recivedQuizData() {

        val questionTimer =Handler(Looper.getMainLooper())
        var totalQuestions: Int = 0
        var questionslist: List<Question>? = ArrayList()
        var listofAllAnswersObject: ArrayList<Answers>


        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

               // getting thedata JSON as a class object
                val data: String? = intent.extras!!.getString("jsonresponse")
                val listType: Type = object : TypeToken<QuestionsAndAnswers>() {}.type
                val gson = Gson()
                val questionsAndAnswers: QuestionsAndAnswers = gson.fromJson(data, listType)
                questionslist = questionsAndAnswers.questions

                // getting all the answers
                listofAllAnswersObject = ArrayList()
                questionslist!!.forEach {
                    // adding the counter to get the total number of questions
                    totalQuestions++

                    // adding the list of answers then adding them to a greater list of all answers for each question
                    it.answers?.let { it1 -> listofAllAnswersObject.add(it1) }


                }

                //initiate the basic question template
                QuestionpageInitiator(questionTimer,questionslist!!,totalQuestions,listofAllAnswersObject)

            }

        }

        registerReceiver(receiver, IntentFilter("data"))


    }

    fun QuestionpageInitiator(questionTimer:Handler,questionslist:List<Question>,totalQuestions:Int,listofAllAnswersObject:ArrayList<Answers>) {

        val currentQuestionIndex: Int = -1
        val adapter :AnswersListAdapter? = null
        val answer :Boolean ?= null
        var answersList:ArrayList<String> = ArrayList<String>()
        val arrayOfLetters = resources.getStringArray(R.array.answer_letters)

        // initialize the question timer
        val timerobj  =Timers()
        questionTimer.let { timerobj.questiontimer(it) }



        // initilize the Question data
        val questionInitializer = QuestionDataInitializer(this@QuizPage,questionslist,binding,currentQuestionIndex,listofAllAnswersObject,arrayOfLetters,adapter,answer,answersList, 0,totalQuestions, 0,dialogProgress,questionTimer)

        val answersHashMap = questionInitializer.getAnswershashmap()
        answersList = questionInitializer.getAnswersList()
        val correctAnswer = questionInitializer.getCorrecrAnswer()


        //initiate the listitem listener to control navigation

       ListInitializer(binding.answersList,answersList,questionTimer,answersHashMap,correctAnswer,this@QuizPage,dialogProgress)



    }


    // todo modulate the QuestionDatainitializer further and maybe add interface between it and listInitializer class
    // todo add the timers without sending them extra data
    // todo smoke test
    // todo add workmanager API
    // todo re inspect the code






}

