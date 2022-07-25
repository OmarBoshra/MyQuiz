package com.example.myquiz.activities.quizPage.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiz.databinding.RecyclerRowBinding
import com.example.myquiz.models.RecyclerData


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private var listData: ArrayList<RecyclerData>? = null
    private lateinit var mListener2: (String, Int) -> Unit


    fun setUpdatedData(listData: ArrayList<RecyclerData>) {
        this.listData = listData
    }

    class MyViewHolder(
        binding: RecyclerRowBinding,
        listener: (String, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var answer: String = ""

        private val buttonView = binding.recyclerButtonView

        fun bind(data: RecyclerData) {// view the speicifed data on the recycler view
            answer = data.answer
            buttonView.text = answer

        }

        init {
            buttonView.setOnClickListener {
                listener.invoke(answer, adapterPosition)

            }
            buttonView.setOnTouchListener { v: View, m: MotionEvent ->
                v.isSelected = m.action != MotionEvent.ACTION_UP
                false
            }

        }

    }
// if I want to interface directly with the recycler view
//    interface onItemClickListener{
//
//        fun onItemClick(position:Int){
//
//
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, mListener2)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listData!![position])
    }

    override fun getItemCount(): Int {
        return if (listData == null)
            0
        else listData?.size!!
    }

    fun setOnItemClickListener2(function: (String, Int) -> Unit) {
        mListener2 = function
    }

}