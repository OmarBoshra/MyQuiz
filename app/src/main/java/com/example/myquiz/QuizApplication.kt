package com.example.myquiz

import android.app.Application
import android.content.Context
import com.example.myquiz.dependencyInjection.RetroModule
import com.example.myquiz.interfaces.DaggerRetroComponent
import com.example.myquiz.interfaces.RetroComponent




class QuizApplication:Application() {


    private lateinit var retroComponent: RetroComponent
    override fun onCreate() {
        super.onCreate()

            retroComponent = DaggerRetroComponent.builder()
                .retroModule(RetroModule())
                .build()

    }

    fun getRetroComponent(): RetroComponent {

        return retroComponent

    }

}