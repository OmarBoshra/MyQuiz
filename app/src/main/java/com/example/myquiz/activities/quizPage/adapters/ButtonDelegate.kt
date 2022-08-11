package com.example.myquiz.activities.quizPage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiz.R
import com.example.myquiz.interfaces.DisplayableItem
import com.example.myquiz.models.ButtonItem
import com.example.myquiz.models.RecyclerData
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class ButtonDelegate :AdapterDelegate<List<RecyclerData>>(){
    override fun isForViewType(items: List<RecyclerData>, position: Int): Boolean {
        return items[position] is ButtonItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ButtonHolder(LayoutInflater.from(parent.context).inflate(R.layout.widget_list_item, parent, false))

    }

    override fun onBindViewHolder(
        items: List<RecyclerData>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        TODO("Not yet implemented")
    }
}