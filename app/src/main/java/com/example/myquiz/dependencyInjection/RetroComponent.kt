package com.example.myquiz.dependencyInjection

import com.example.myquiz.models.QuizPageViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface RetroComponent {

    fun inject(quizPageViewModel: QuizPageViewModel)
}