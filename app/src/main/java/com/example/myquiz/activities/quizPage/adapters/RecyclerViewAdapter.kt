package com.example.myquiz.activities.quizPage.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiz.activities.quizPage.views.ListListener
import com.example.myquiz.databinding.RecyclerRowBinding
import com.example.myquiz.models.RecyclerData


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private var listData: ArrayList<RecyclerData>? = null
    private lateinit var mListener: ListListener
    private lateinit var viewGroup: ViewGroup

    private var selectedPos = RecyclerView.NO_POSITION

    fun setUpdatedData(listData: ArrayList<RecyclerData>) {
        this.listData = listData

    }


    class MyViewHolder(
        binding: RecyclerRowBinding,
        listener: ListListener,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {
        val buttonView = binding.recyclerButtonView

        fun bind(data: RecyclerData) {// view the speicifed data on the recycler view
            buttonView.text = data.answer
        }

        init {
            buttonView.setOnClickListener {

                listener.onItemClick(it, this, parent)

            }
//            buttonView.setOnTouchListener {v: View, m: MotionEvent ->
//                listener.onTouch(v)
//                true
//            }

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

    fun setOnItemClickListener(listener: ListListener) {

        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        viewGroup = parent

        return MyViewHolder(binding, mListener, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listData!![position])
        holder.itemView.setSelected(selectedPos == position);


    }

    override fun getItemCount(): Int {
        return if (listData == null)
            0
        else listData?.size!!


    }

}