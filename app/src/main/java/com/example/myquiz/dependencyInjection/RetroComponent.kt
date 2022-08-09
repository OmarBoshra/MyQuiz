package com.example.myquiz.dependencyInjection


import com.example.myquiz.GettingQuestionsUseCase
import com.example.myquiz.activities.quizPage.QuizPage
import com.example.myquiz.models.QuizPageViewModel
import com.example.myquiz.models.QuizPageViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface RetroComponent {

    fun injectViewModel(quizPageViewModel: QuizPageViewModel)
    fun injectActivity(quizPageActivity: QuizPage)
}