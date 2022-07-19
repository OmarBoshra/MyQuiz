package com.example.myquiz.activities.quizPage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiz.activities.quizPage.views.ListListener
import com.example.myquiz.databinding.RecyclerviewRowitemBinding
import com.example.myquiz.models.RecyclerData
import kotlinx.coroutines.NonDisposableHandle.parent


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.myViewHolder>() {

   private var listData: ArrayList<RecyclerData>? = null
   private lateinit var mListener: ListListener

        fun setUpdatedData (listData: ArrayList<RecyclerData>){
            this.listData = listData

        }

        class myViewHolder (binding : RecyclerviewRowitemBinding, listener: ListListener): RecyclerView.ViewHolder(binding.root){
            val textView = binding.recyclerTextView

            fun bind(data: RecyclerData){// view the speicifed data on the recycler view
                    textView.setText(data.answer)

/*                Glide.with(imageView)
                    .load(data.owner?.avatar_url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)*/


            }

            init {
                itemView.setOnClickListener{
                    listener.onItemClick(adapterPosition)

                }

            }


    }

    interface onItemClickListener{

        fun onItemClick(position:Int){


        }
    }

    fun setOnItemClickListener(listener: ListListener){

        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

       val binding = RecyclerviewRowitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)



        return myViewHolder(binding,mListener)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
            holder.bind(listData!!.get(position))



    }

    override fun getItemCount(): Int {
        if(listData == null)
            return 0
        else return listData?.size!!

    }


}