package com.example.myquiz

import android.app.ProgressDialog
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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.models.Answers
import com.example.myquiz.models.Question
import com.example.myquiz.models.QuestionsAndAnswers
import com.example.myquiz.widgets.Check24_ProgressBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.Executors


class QuizPage : AppCompatActivity() {

    private lateinit var binding: QuizPageBinding
//    private lateinit var widgetBinding: widget

    private var questionScore: Int = 0
    private var totalScore: Int = 0
    private var currentQuestionIndex: Int = 0
    private var TotalQuestions: Int = 0
    private var questionslist: List<Question>? = ArrayList()
    private var AnswersHashMap: HashMap<String, String> = HashMap()
    private var questionTimer: Handler? = null


    private lateinit var dialogProgress: Check24_ProgressBar
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var answersList: ArrayList<String>
    private lateinit var correctAnswer: String


    private lateinit var listofAllAnswers: ArrayList<Answers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = QuizPageBinding.inflate(layoutInflater);
        setContentView(binding.root)

//        binding.questionImage.setImageBitmap()
//        binding.question.setText("")
//        binding.numberOfPoints.setText("")
//        binding.questionIndicator.setText("")

        // initial setting of shared preferences
        var pref: SharedPreferences

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


        var receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val data: String? = intent.extras!!.getString("jsonresponse")


                val listType: Type = object : TypeToken<QuestionsAndAnswers>() {}.type

                var gson = Gson()


                var QuestionsAndAnswers: QuestionsAndAnswers = gson.fromJson(data, listType)

                questionslist = QuestionsAndAnswers.questions

                if (questionslist != null) {

                    listofAllAnswers = ArrayList()
                    questionslist?.forEach {
                        // adding the counter to get the total number of questions
                        TotalQuestions++

                        // adding the list of answers then adding them to a greater list of all answers for each question
                        it.answers?.let { it1 -> listofAllAnswers.add(it1) }


                    }

                    // displaying the answers
                    answersList = ArrayList()



                    listofAllAnswers.get(0).A?.let {
                        answersList.add(it)
                        AnswersHashMap.put("A", it)

                    }

                    listofAllAnswers.get(0).B?.let {
                        answersList.add(it)

                        AnswersHashMap.put("B", it)
                    }
                    listofAllAnswers.get(0).C?.let {
                        answersList.add(it)
                        AnswersHashMap.put("C", it)

                    }
                    listofAllAnswers.get(0).D?.let {
                        answersList.add(it)

                        AnswersHashMap.put("D", it)
                    }

                    Collections.shuffle(answersList);
                    // initialize an array adapter

                    adapter = ArrayAdapter(
                        this@QuizPage,
                        R.layout.widget_list_item, answersList
                    )

                    binding.answersList.adapter = adapter

                    // initializing the header
                    binding.questionIndicator.setText("Frage " + (currentQuestionIndex + 1) + "/$TotalQuestions - Aktuelle Punktzahl: $totalScore")

                    // initialize the progressIndicator
                    binding.progresIndicator.max = TotalQuestions

                    // initialize the question score
                    questionScore = questionslist!!.get(0).score!!
                    binding.numberOfPoints.setText("$questionScore Punkte")

                    //initialize the question
                    binding.question.setText(questionslist!!.get(0).question)
                    correctAnswer = questionslist!!.get(0).correctAnswer.toString()

                    // initializing the image

                    questionslist!!.get(0).questionImageUrl?.let {
                        DownloadImageFromInternet(
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


        registerReceiver(receiver, IntentFilter("data"));


    }


    private fun navigation() {


        // if user doesn't answer in 10 seconds ,consider it a wroung answer and go to next question

        questionTimer= Handler()
        questionTimer!!.postDelayed({

            nextQuestion(false)

        }, 10000)



        binding.answersList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // This is your listview's selected item
                val item = parent.getItemAtPosition(position)
                if (parent.isPressed) {

                    // disable the timer

                        questionTimer?.removeCallbacksAndMessages(null)



                    var answer: Boolean = false



                    if (item.toString().equals(AnswersHashMap.get(correctAnswer))) {
                        Toast.makeText(
                            applicationContext,
                            "Correct answer",
                            Toast.LENGTH_SHORT
                        ).show()
                        answer = true
                        view.setBackgroundColor(resources.getColor(R.color.green))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "InCorrect answer",
                            Toast.LENGTH_SHORT
                        ).show()
                        view.setBackgroundColor(resources.getColor(R.color.red))
                        parent.getChildAt(answersList.indexOf(AnswersHashMap.get(correctAnswer)))
                            .setBackgroundColor(resources.getColor(R.color.green))

                        answer = false

                    }

                    parent.isEnabled = false


                    dialogProgress.setCancelable(false);
                    dialogProgress.show();

                    Handler().postDelayed({
                        parent.isEnabled = true

                        view.background = AppCompatResources.getDrawable(this, R.drawable.button_default)
                        AppCompatResources.getDrawable(this, R.drawable.button_default)


                        parent.getChildAt(answersList.indexOf(AnswersHashMap.get(correctAnswer))).background =
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



        if (currentQuestionIndex < listofAllAnswers.size) {

            // update progress indicator
            binding.progresIndicator.setProgress(currentQuestionIndex)

            // show the new answers for the next question

            answersList.clear()


            AnswersHashMap = HashMap()
            listofAllAnswers.get(currentQuestionIndex).A?.let {
                answersList.add(it)
                AnswersHashMap.put("A", it)

            }

            listofAllAnswers.get(currentQuestionIndex).B?.let {
                answersList.add(it)

                AnswersHashMap.put("B", it)
            }
            listofAllAnswers.get(currentQuestionIndex).C?.let {
                answersList.add(it)
                AnswersHashMap.put("C", it)

            }
            listofAllAnswers.get(currentQuestionIndex).D?.let {
                answersList.add(it)

                AnswersHashMap.put("D", it)
            }
            Collections.shuffle(answersList)


            adapter.notifyDataSetChanged()


            // change the header
            if (answer)
                totalScore = totalScore + questionScore

            binding.questionIndicator.setText("Frage " + (currentQuestionIndex + 1) + "/$TotalQuestions - Aktuelle Punktzahl: $totalScore")

            // change the number of points
            questionScore = questionslist?.get(currentQuestionIndex)?.score!!
            binding.numberOfPoints.setText("$questionScore Punkte")

            // change the question

            binding.question.setText(questionslist!!.get(currentQuestionIndex).question)


            // change the image

//        Picasso.get().load(questionslist!!.get(currentQuestionIndex).questionImageUrl).into(LogoAriline)


            questionslist!!.get(currentQuestionIndex).questionImageUrl?.let {

                if (it.equals("null"))
                    dialogProgress.dismiss()

                DownloadImageFromInternet(
                    binding.questionImage,
                    it
                )
            } ?: kotlin.run {
                dialogProgress.dismiss()
            }
            correctAnswer = questionslist!!.get(currentQuestionIndex).correctAnswer.toString()


// if the user is at the last question
        } else if (currentQuestionIndex == listofAllAnswers.size) {


            var pref: SharedPreferences = getSharedPreferences("MyPref", 0)

            if (pref.getInt("HighestScore", 0) < totalScore) {
                pref = applicationContext.getSharedPreferences("MyPref", 0)
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putInt("HighestScore", totalScore)
                editor.apply()
            }

            dialogProgress.dismiss()

            val NavigtionIntent = Intent(this@QuizPage, MainActivity::class.java)
            startActivity(NavigtionIntent);


        }




    }

    fun DownloadImageFromInternet(imageView: AppCompatImageView, url: String) {


        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null

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
//    private inner class DownloadImageFromInternet(
//        var imageView: AppCompatImageView,
//        var url: String
//    ) : AsyncTask<String, Void, Bitmap?>() {
//        init {
//            Toast.makeText(
//                applicationContext,
//                "Please wait, it may take a few minutes...",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//        override fun doInBackground(vararg urls: String): Bitmap? {
//            val imageURL = url
//            var image: Bitmap? = null
//            try {
//                val `in` = java.net.URL(imageURL).openStream()
//                image = BitmapFactory.decodeStream(`in`)
//            } catch (e: Exception) {
//                Log.e("Error Message", e.message.toString())
//                e.printStackTrace()
//            }
//            return image
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//            imageView.setImageBitmap(result)
//            dialogProgress.dismiss()
//        }
//    }

}

