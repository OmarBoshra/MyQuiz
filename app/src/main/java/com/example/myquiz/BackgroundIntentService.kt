package com.example.myquiz

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class BackgroundIntentService : IntentService("BackgroundIntentService") {

    private val LOG_TAG = "BackgroundIntentService"

    private var mQueue: RequestQueue? = null


    //    constructor() : super(1)
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate")

    }

    private fun jsonParse(url: String, mQueue: RequestQueue) {

        var jsonobjectString: String = ""


        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    jsonobjectString = response.toString()

                    jsonobjectString?.let { Log.d("TAG", it) };

                    val i = Intent("data")
                    i.putExtra("jsonresponse", jsonobjectString)
                    sendBroadcast(i);


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }



        mQueue.add(request)


    }

    override fun onHandleIntent(intent: Intent?) {


        mQueue = Volley.newRequestQueue(applicationContext)

        val url = resources.getString(R.string.url)
        jsonParse(url, mQueue!!)


//        val intent = Intent()
//        intent.putExtra("json","ok")
//        intent.action = "com.example.myQuize.INCOMING_OBJECR"
//// put your data in intent
//
//        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)


    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
