package com.example.myquiz.activities.QuizPage

import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myquiz.R
import com.example.myquiz.activities.MainActivity
import com.example.myquiz.activities.QuizPage.Adapters.AnswersListAdapter
import com.example.myquiz.activities.QuizPage.Views.ListListener
import com.example.myquiz.activities.QuizPage.Views.QuestionDataInitializer
import com.example.myquiz.databinding.QuizPageBinding

import com.example.myquiz.models.Question
import com.example.myquiz.models.QuestionsAndAnswers
import com.example.myquiz.custom_widgets.Check24ProgressBar
import com.example.myquiz.interfaces.ApiInterface
import com.example.myquiz.models.QuizPageData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


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
//        dialogProgress.show()

        // initiate receiving the quiz data
        recivedQuizData()




    }

    fun recivedQuizData() {

        var questionslist: List<Question>?



        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

               // getting thedata JSON as a class object

                getRetroData()

                val data: String? = intent.extras!!.getString("jsonresponse")
                val listType: Type = object : TypeToken<QuestionsAndAnswers>() {}.type
                val gson = Gson()
                val questionsAndAnswers: QuestionsAndAnswers = gson.fromJson(data, listType)

                questionslist = questionsAndAnswers.questions
                var quizPageData = QuizPageData()


                // start the quiz
                var islastQuestion = false // to stop at the last question


                var answersList:ArrayList<String> =ArrayList()
                // initialize an list adapter

               var adapter =  AnswersListAdapter(
                    this@QuizPage,
                    R.layout.widget_list_item, answersList
                )

                binding.answersList.adapter = adapter
                // initilize list itemlistener

                 ListListener(
                    quizPageData,
                    binding.answersList,
                    this@QuizPage,
                    dialogProgress
                )


//                quizPageData = listobj.quizPageData


                    val questionObj = QuestionDataInitializer(
                        this@QuizPage,
                        questionslist!!,quizPageData, dialogProgress
                    )

                adapter.addItems(questionObj.getList())
                adapter.notifyDataSetChanged()



                // checking last question and updating the quiz data
                    islastQuestion= questionObj.lastQuestion()
                    quizPageData = questionObj.quizPageData


                    val questionScore = questionslist!![0].score!!
                    val totalQuestions = questionslist!!.size
                    val question = questionslist!![0].question
                    questionRenderer(question,questionScore, totalQuestions,quizPageData)

                if(islastQuestion) {
                    val navigtionIntent = Intent(this@QuizPage, MainActivity::class.java)
                    startActivity(navigtionIntent)
                }


                //    val timerobj = Timers()
//                    timerobj.questiontimer()





            }

        }

        registerReceiver(receiver, IntentFilter("data"))


    }

    private fun getRetroData(){

        val retrofitBuilder = Retrofit.Builder().
        addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://app.check24.de/vg2-quiz/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitdata = retrofitBuilder.getdata()

        retrofitdata.enqueue(object : Callback<QuestionsAndAnswers?> {
            override fun onResponse(
                call: Call<QuestionsAndAnswers?>,
                response: Response<QuestionsAndAnswers?>
            ) {

                val responseBody = response.body()

                val StringBuilder = StringBuilder()
                if (responseBody != null) {
                    responseBody.questions.forEach {



                    }



                    }
            }

            override fun onFailure(call: Call<QuestionsAndAnswers?>, t: Throwable) {


            }
        })


    }
    private fun questionRenderer(
        question: String?,
        questionScore: Int,
        totalQuestions: Int,
        quizPageData: QuizPageData
    ) {

// set the question image
        binding.questionImage.setImageBitmap(quizPageData.questionImageBitMap)
        // set the quizheader
        val questionIndicatorText: StringBuilder = StringBuilder()

        questionIndicatorText.append("Frage ")
        questionIndicatorText.append(quizPageData.currentQuestionIndex + 1)
        questionIndicatorText.append("/")
        questionIndicatorText.append(totalQuestions)
        questionIndicatorText.append(" - Aktuelle Punktzahl: ")
        questionIndicatorText.append(quizPageData.totalScore)

        binding.questionIndicator.setText(questionIndicatorText)

        // initialize the progressIndicator

        binding.progresIndicator.max = totalQuestions

        // initialize the question score


        val questionScoreText: StringBuilder = StringBuilder()
        questionScoreText.append(questionScore)
        questionScoreText.append(" Punkte")
        binding.numberOfPoints.text = questionScoreText

        //initialize the question
        binding.question.text =question




    }

//    fun QuestionpageInitiator(questionslist) {
//
//        val currentQuestionIndex: Int = -1
//        val adapter :AnswersListAdapter? = null
//        val answer :Boolean ?= null
//
//
//        // initialize the question timer
//        val questionTimer = Timers()
//        questionTimer.questiontimer()
//
//
//
//        // initilize the Question data
//
//
//
//        val answersHashMap = questionInitializer.getAnswershashmap()
//        answersList = questionInitializer.getAnswersList()
//        val correctAnswer = questionInitializer.getCorrecrAnswer()
//
//
//        //initiate the listitem listener to control navigation
//
//       ListInitializer(binding.answersList,answersList,answersHashMap,correctAnswer,this@QuizPage,dialogProgress)
//
//
//
//
//    }


    // todo modulate the QuestionDatainitializer further and maybe add interface between it and listInitializer class
    // todo add the timers without sending them extra data
    // todo smoke test
    // todo add workmanager API
    // todo re inspect the code






}

