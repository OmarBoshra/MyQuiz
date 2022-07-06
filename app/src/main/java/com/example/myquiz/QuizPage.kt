package com.example.myquiz

import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.models.Answers
import com.example.myquiz.models.Question
import com.example.myquiz.models.QuestionsAndAnswers
import com.example.myquiz.custom_widgets.Check24_ProgressBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.Executors


class QuizPage : AppCompatActivity() {

    private lateinit var binding: QuizPageBinding

    // private lateinit var widgetBinding: widget

    private var questionScore: Int = 0
    private var totalScore: Int = 0
    private var currentQuestionIndex: Int = 0
    private var totalQuestions: Int = 0
    private var questionslist: List<Question>? = ArrayList()
    private var answersHashMap: HashMap<String, String> = HashMap()
    private var questionTimer: Handler? = null


    private lateinit var dialogProgress: Check24_ProgressBar
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var answersList: ArrayList<String>
    private lateinit var correctAnswer: String


    private lateinit var listofAllAnswersObject: ArrayList<Answers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = QuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // initial setting of shared preferences
        val pref: SharedPreferences

        if (!applicationContext.getSharedPreferences("MyPref", 0).contains("HighestScore")) {
            pref = applicationContext.getSharedPreferences("MyPref", 0)
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putInt("HighestScore", 0)
            editor.apply()
        } else {

            currentQuestionIndex = 0
        }



        dialogProgress = Check24_ProgressBar(this@QuizPage)
        dialogProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress.show()


        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val data: String? = intent.extras!!.getString("jsonresponse")


                val listType: Type = object : TypeToken<QuestionsAndAnswers>() {}.type

                val gson = Gson()


                val questionsAndAnswers: QuestionsAndAnswers = gson.fromJson(data, listType)

                questionslist = questionsAndAnswers.questions

                if (questionslist != null) {

                    listofAllAnswersObject = ArrayList()
                    questionslist?.forEach {
                        // adding the counter to get the total number of questions
                        totalQuestions++

                        // adding the list of answers then adding them to a greater list of all answers for each question
                        it.answers?.let { it1 -> listofAllAnswersObject.add(it1) }


                    }

                    // displaying the answers
                    answersList = ArrayList()


                    // fill the list of answers


                    resources.getStringArray(R.array.answer_letters).forEach {

                        val alphabet = it

                        listofAllAnswersObject[0].letterchecker(alphabet)?.let { it1 ->

                            answersList.add(it1)

                            answersHashMap[alphabet] = it1

                        }

                    }


                    answersList.shuffle()
                    // initialize an array adapter

                    adapter = ArrayAdapter(
                        this@QuizPage,
                        R.layout.widget_list_item, answersList
                    )

                    binding.answersList.adapter = adapter

                    val quizHeaderText: StringBuilder = StringBuilder()
                    quizHeaderText.append("Frage ")
                    quizHeaderText.append(currentQuestionIndex + 1)
                    quizHeaderText.append("/")
                    quizHeaderText.append(totalQuestions)
                    quizHeaderText.append(" - Aktuelle Punktzahl: ")
                    quizHeaderText.append(totalScore)

                    // initializing the header
                    binding.questionIndicator.text = quizHeaderText

                    // initialize the progressIndicator
                    binding.progresIndicator.max = totalQuestions

                    // initialize the question score
                    questionScore = questionslist!![0].score!!

                    val questionScoreText: StringBuilder = StringBuilder()
                    questionScoreText.append(questionScore)
                    questionScoreText.append(" Punkte")
                    binding.numberOfPoints.text = questionScoreText

                    //initialize the question
                    binding.question.text = questionslist!![0].question
                    correctAnswer = questionslist!![0].correctAnswer.toString()

                    // initializing the image

                    questionslist!![0].questionImageUrl?.let {
                        downloadImageFromInternet(
                            binding.questionImage,
                            it
                        )
                    } ?: kotlin.run {
                        dialogProgress.dismiss()
                    }


                    //initiate the listitem listener to control navigation
                    navigation()

                }

            }

        }


        registerReceiver(receiver, IntentFilter("data"))


    }
    //todo make sure sore is working
    //todo inspect the code
    //todo modulate

    private fun timer (){

        // if user doesn't answer in 10 seconds ,consider it a wroung answer and go to next question
        questionTimer = Handler(Looper.getMainLooper())
        questionTimer!!.postDelayed({

            nextQuestion(false)

        }, 10000)

    }

    private fun navigation() {

        timer()

        binding.answersList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, _ ->
                // This is your listview's selected item
                val item = parent.getItemAtPosition(position)
                if (parent.isPressed) {

                    // disable the timer

                    questionTimer?.removeCallbacksAndMessages(null)


                    val answer: Boolean



                    if (item.toString() == answersHashMap[correctAnswer]) {
                        Toast.makeText(
                            applicationContext,
                            "Correct answer",
                            Toast.LENGTH_SHORT
                        ).show()
                        answer = true
                        view.setBackgroundColor(
                            ContextCompat.getColor(
                                this@QuizPage,
                                R.color.green
                            )
                        )
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "InCorrect answer",
                            Toast.LENGTH_SHORT
                        ).show()
                        view.setBackgroundColor(ContextCompat.getColor(this@QuizPage, R.color.red))
                        parent.getChildAt(answersList.indexOf(answersHashMap[correctAnswer]))
                            .setBackgroundColor(
                                ContextCompat.getColor(
                                    this@QuizPage,
                                    R.color.green
                                )
                            )

                        answer = false

                    }


                    parent.isEnabled = false


                    dialogProgress.setCancelable(false)
                    dialogProgress.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        parent.isEnabled = true

                        view.background =
                            AppCompatResources.getDrawable(this, R.drawable.button_default)
                        AppCompatResources.getDrawable(this, R.drawable.button_default)


                        parent.getChildAt(answersList.indexOf(answersHashMap[correctAnswer])).background =
                            AppCompatResources.getDrawable(this, R.drawable.button_default)

                        nextQuestion(answer)


                    }, 2000)

                }


            }


    }

    private fun nextQuestion(answer: Boolean) {

        // reset the image
        binding.questionImage.setImageBitmap(null)

        // next question counter
        currentQuestionIndex++



        if (currentQuestionIndex < listofAllAnswersObject.size) {

            // update progress indicator
            binding.progresIndicator.progress = currentQuestionIndex

            // show the new answers for the next question

            answersList.clear()


            answersHashMap = HashMap()

            // fill the list of answers
            resources.getStringArray(R.array.answer_letters).forEach {

                val alphabet = it

                listofAllAnswersObject[0].letterchecker(alphabet)?.let { it1 ->

                    answersList.add(it1)

                    answersHashMap[alphabet] = it1

                }

            }
            answersList.shuffle()


            adapter.notifyDataSetChanged()


            // change the header
            if (answer)
                totalScore += questionScore

            val quizHeaderText: StringBuilder = StringBuilder()
            quizHeaderText.append("Frage ")
            quizHeaderText.append(currentQuestionIndex + 1)
            quizHeaderText.append("/")
            quizHeaderText.append(totalQuestions)
            quizHeaderText.append(" - Aktuelle Punktzahl: ")
            quizHeaderText.append(totalScore)


            binding.questionIndicator.text = quizHeaderText


            // change the number of points
            questionScore = questionslist?.get(currentQuestionIndex)?.score!!


            val questionScoreText: StringBuilder = StringBuilder()
            questionScoreText.append(questionScore)
            questionScoreText.append(" Punkte")
            binding.numberOfPoints.text = questionScoreText

            binding.numberOfPoints.text = questionScoreText

            // change the question

            binding.question.text = questionslist!![currentQuestionIndex].question



            questionslist!![currentQuestionIndex].questionImageUrl?.let {

                if (it == ("null"))
                    dialogProgress.dismiss()

                downloadImageFromInternet(
                    binding.questionImage,
                    it
                )
            } ?: kotlin.run {
                dialogProgress.dismiss()
            }
            correctAnswer = questionslist!![currentQuestionIndex].correctAnswer.toString()


// if the user is at the last question
        } else if (currentQuestionIndex == listofAllAnswersObject.size) {


            var pref: SharedPreferences = getSharedPreferences("MyPref", 0)

            if (pref.getInt("HighestScore", 0) < totalScore) {
                pref = applicationContext.getSharedPreferences("MyPref", 0)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putInt("HighestScore", totalScore)
                editor.apply()
            }

            dialogProgress.dismiss()

            val navigtionIntent = Intent(this@QuizPage, MainActivity::class.java)
            startActivity(navigtionIntent)


        }
        timer()

    }

    fun downloadImageFromInternet(imageView: AppCompatImageView, url: String) {

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap?

        executor.execute {

            // Image URL

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(url).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    imageView.setImageBitmap(image)
                    dialogProgress.dismiss()
                }
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }


}

