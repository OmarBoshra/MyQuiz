package com.example.myquiz

import android.app.Application
import com.example.myquiz.dependencyInjection.DaggerRetroComponent
import com.example.myquiz.dependencyInjection.RetroComponent
import com.example.myquiz.dependencyInjection.RetroModule
import com.example.myquiz.models.QuizPageViewModelFactory

class QuizApplication : Application() {

    private lateinit var retroComponent: RetroComponent
    private lateinit var retroComponent2: RetroComponent

    override fun onCreate() {

        super.onCreate()

        retroComponent = DaggerRetroComponent.builder()
            .retroModule(RetroModule())
            .build()
        retroComponent2 = DaggerRetroComponent.builder()
            .build()
    }

    fun getRetroComponent2(): RetroComponent {

        return retroComponent2

    }
    fun getRetroComponent(): RetroComponent {

        return retroComponent

    }

}