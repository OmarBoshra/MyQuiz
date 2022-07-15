package com.example.myquiz.activities.quizPage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myquiz.databinding.RecyclerviewRowitemBinding
import com.example.myquiz.models.RecyclerData


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.myViewHolder>() {

   private var listData: RecyclerData? = null

        fun setUpdatedData (listData: RecyclerData){
            this.listData = listData

        }

        class myViewHolder (binding : RecyclerviewRowitemBinding): RecyclerView.ViewHolder(binding.root){
            val textView = binding.recyclerTextView

            fun bind(data: String){// view the speicifed data on the recycler view
                    textView.setText(data)

/*                Glide.with(imageView)
                    .load(data.owner?.avatar_url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)*/


            }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val binding: RecyclerviewRowitemBinding

        binding = RecyclerviewRowitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return myViewHolder(binding)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
            holder.bind(listData?.get(position)!!)



    }

    override fun getItemCount(): Int {
        if(listData == null)
            return 0
        else return listData?.size!!

    }


}