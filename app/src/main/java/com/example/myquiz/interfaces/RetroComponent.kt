package com.example.myquiz.interfaces

import com.example.myquiz.dependencyInjection.RetroModule
import com.example.myquiz.models.QuizPageViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [RetroModule::class])
interface RetroComponent {

    fun inject(quizPageViewModel: QuizPageViewModel)
}