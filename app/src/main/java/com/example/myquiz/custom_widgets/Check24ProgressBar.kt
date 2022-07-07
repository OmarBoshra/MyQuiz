package com.example.myquiz.custom_widgets

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StyleRes
import com.example.myquiz.databinding.WidgetProgressbarBinding


class Check24ProgressBar @JvmOverloads constructor(
    context: Context,
    @StyleRes themeResId: Int = 0
) : Dialog(context, themeResId) {

    private var binding: WidgetProgressbarBinding =
        WidgetProgressbarBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(binding.root)
    }


//    fun setSuccess(){
//        binding.textView.setText("")
//
//
//    }


}