package com.example.myquiz.utilities

import java.util.*


class Timers {

    var timer: Timer = Timer()


    private fun reminder(seconds: Long) {
        timer = Timer()
        timer.schedule(RemindTask(), seconds * 1000)

    }

    inner class RemindTask : TimerTask() {
        override fun run() {
            println("Time's up!")

            timer.cancel() //Terminate the timer thread
        }
    }

    fun questiontimer() {
        reminder(10)

    }

    fun questionWaitTimer() {
        reminder(2)

    }

    fun cancelTimer() {
        timer.cancel() //Terminate the timer thread

    }


}


