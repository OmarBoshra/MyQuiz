package com.example.myquiz

import android.app.Application
import com.example.myquiz.dependencyInjection.DaggerRetroComponent
import com.example.myquiz.dependencyInjection.RetroComponent
import com.example.myquiz.dependencyInjection.RetroModule

class QuizApplication : Application() {

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