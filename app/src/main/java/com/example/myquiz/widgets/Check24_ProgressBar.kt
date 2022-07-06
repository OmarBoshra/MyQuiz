package com.example.myquiz.widgets

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import com.example.myquiz.databinding.QuizPageBinding
import com.example.myquiz.databinding.WidgetProgressbarBinding


class Check24_ProgressBar @JvmOverloads constructor(context: Context, @StyleRes themeResId: Int = 0) : Dialog(context,themeResId){

    private var binding: WidgetProgressbarBinding = WidgetProgressbarBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(binding.root)
    }


//    fun setSuccess(){
//        binding.textView.setText("")
//
//
//    }





}