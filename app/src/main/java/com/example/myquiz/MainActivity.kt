package com.example.myquiz

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.myquiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mQueue: RequestQueue? = null
    lateinit var pref: SharedPreferences

    public val LOG_TAG: String = "MainActivity";
    public val BackgroundIntentServiceAction = "android.intent.action.CUSTOME_ACTION_1"


//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//
//
//        return super.onCreateView(name, context, attrs)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

//        button1 = (findViewById(R.id.start_button))

        mQueue = Volley.newRequestQueue(this@MainActivity)

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        var highestScore = pref.getInt("HighestScore", 0)
        binding.highscoreTextview.setText("HighScore " + (highestScore))

//        this@MainActivity.localBroadcastReceiver = BroadcastReceiver() {
//            override fun onReceive(Context context, Intent intent) {
//                // Do what you have to do here if you receive data from the Service.
//            }
//        }

        binding.startButton.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View): Unit {
                // Handler code here.

                val serviceIntent =
                    Intent(this@MainActivity, BackgroundIntentService::class.java).apply {
                        intent.setAction(BackgroundIntentServiceAction)

                    }

                startService(serviceIntent)


                val NavigtionIntent = Intent(this@MainActivity, QuizPage::class.java)
                startActivity(NavigtionIntent);
            }


        })


    }

//    private fun initReciver(){
//
//        val reciver = ContextBasedBroadcastReciver()
//
//    }
}