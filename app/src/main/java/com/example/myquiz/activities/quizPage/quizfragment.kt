package com.example.myquiz.activities.quizPage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myquiz.R
import com.example.myquiz.activities.MainActivity
import com.example.myquiz.activities.quizPage.adapters.RecyclerViewAdapter
import com.example.myquiz.customWidgets.Check24ProgressBar
import com.example.myquiz.databinding.FragmentQuizfragmentBinding
import com.example.myquiz.models.QuizPageUIData
import com.example.myquiz.models.QuizPageViewModel
import com.example.myquiz.models.RecyclerData


import com.example.myquiz.models.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [quizfragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class quizfragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var currentpostionofFragment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }
    private lateinit var binding: FragmentQuizfragmentBinding
    private lateinit var dialogProgress: Check24ProgressBar
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var quizPageViewModel: QuizPageViewModel
    private lateinit var quizPageUIData: QuizPageUIData

    private var questionTimer: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
            /**
             * # quizFragment
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


        // initialize the UI binding
        binding = FragmentQuizfragmentBinding.inflate(layoutInflater)


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment quizfragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            quizfragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {

        // disable the timer
        if (questionTimer != null) {
            questionTimer!!.removeCallbacksAndMessages(null)
            questionTimer = null
        }
        // update the fragments number
        currentpostionofFragment= (activity as QuizPage).adapter.currentposition
        quizPageViewModel.updateQuestionIndex(currentpostionofFragment)

        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // initiate progress dialogue
        dialogProgress = Check24ProgressBar(requireActivity())
        dialogProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        if(activity!=null) {
            initViewModel()
            iniRecyclerView()
        }

        super.onViewCreated(view, savedInstanceState)
    }


    /**
     * ## initViewModel
     * @since [liveDataForUI][com.example.myquiz.models.QuizPageViewModel.liveDataForUI] is observed
     * * initViewModel Observes the data that should come to the UI
     * * once the data arrives it reinitializes [iniRecyclerView] and [questionDataManager] with the new data
     * */
    private fun initViewModel() {
        quizPageViewModel = ViewModelProvider(this)[QuizPageViewModel::class.java]

        quizPageViewModel.liveDataForUI.observe(requireActivity()) {
            if (it == null) {
                Toast.makeText(requireActivity(), "error in getting data", Toast.LENGTH_SHORT)
                    .show()
            } else {

                quizPageUIData = it
                (activity as QuizPage).adapter.numberOfFragments =quizPageUIData.numberOfQuestions

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
                        requireActivity(),
                        R.color.green
                    )
                )
            } else {
                // make sure to make it red and make the correct answer green
                selectedView?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.red
                    )
                )
                correctAnswerView?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.green
                    )
                )

            }
            // reinitialize the dialog
            dialogProgress.show()
            Handler(Looper.getMainLooper()).postDelayed({

                selectedView?.background =
                    AppCompatResources.getDrawable(requireActivity(), R.drawable.button_default)

                correctAnswerView?.background =
                    AppCompatResources.getDrawable(requireActivity(), R.drawable.button_default)

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
            HighscoreSaver(requireActivity() as QuizPage, quizPageUIData.totalScore)

            val navigtionIntent = Intent(requireActivity(), MainActivity::class.java)
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