package com.example.myquiz.activities.quizPage

import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myquiz.R
import com.example.myquiz.activities.MainActivity
import com.example.myquiz.activities.quizPage.adapters.AnswersListAdapter
import com.example.myquiz.activities.quizPage.views.ListListener
import com.example.myquiz.activities.quizPage.views.QuestionDataInitializer
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.models.Question
import com.example.myquiz.models.QuestionsAndAnswers
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.interfaces.ApiInterface
import com.example.myquiz.interfaces.QuizUIListener
import com.example.myquiz.models.QuizPageData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class QuizPage : AppCompatActivity(), QuizUIListener {

    private lateinit var binding: QuizPageBinding

    private lateinit var dialogProgress: Check24ProgressBar
    private lateinit var questionslist: List<Question>
    private var quizPageData = QuizPageData()

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

    private fun recivedQuizData() {


        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                // getting thedata JSON as a class object

                getRetroData()

                val data: String? = intent.extras!!.getString("jsonresponse")
                val listType: Type = object : TypeToken<QuestionsAndAnswers>() {}.type
                val gson = Gson()
                val questionsAndAnswers: QuestionsAndAnswers = gson.fromJson(data, listType)

                questionslist = questionsAndAnswers.questions
                //initialize progress indicator
                binding.progresIndicator.max = questionslist.size

                // initialize an list adapter
                val answersList: ArrayList<String> = ArrayList()
                val adapter = AnswersListAdapter(
                    this@QuizPage,
                    R.layout.widget_list_item, answersList
                )

                binding.answersList.adapter = adapter
                quizPageData.adapter = adapter // save it to the data model


                // initilize the question initializer
//                val questionObj = QuestionDataInitializer(this@QuizPage,
//                    this@QuizPage,
//                    questionslist!!,quizPageData, dialogProgress
//                )


                // initilize list itemlistener

                binding.answersList.onItemClickListener = ListListener(
                    this@QuizPage,
                    quizPageData,
                    questionslist,
                    this@QuizPage,
                    dialogProgress
                )

                // first initialization of the questionsData
                QuestionDataInitializer(
                    this@QuizPage, context,
                    questionslist, quizPageData, dialogProgress
                )


                //    val timerobj = Timers()
//                    timerobj.questiontimer()


            }

        }

        registerReceiver(receiver, IntentFilter("data"))


    }

    private fun getRetroData() {

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
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

//                val StringBuilder = StringBuilder()
                responseBody?.questions?.forEach {


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


            val questionScore = questionslist[quizPageData.currentQuestionIndex].score!!
            val totalQuestions = questionslist.size
            val question = questionslist[quizPageData.currentQuestionIndex].question
            questionRenderer(question, questionScore, totalQuestions, quizPageData)


        }


    }


    // todo modulate the QuestionDatainitializer further and maybe add interface between it and quizpage activity
    // todo add the timers without sending them extra data
    // todo smoke test
    // todo add workmanager API
    // todo re inspect the code


}

