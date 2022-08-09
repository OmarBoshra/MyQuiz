package com.example.myquiz.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myquiz.GettingQuestionsUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Inject

class QuizPageViewModelFactory @Inject constructor(val requestItemData: GettingQuestionsUseCase): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(QuizPageViewModel::class.java)){
            return QuizPageViewModel(requestItemData) as T
        }
        else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
     }
}
