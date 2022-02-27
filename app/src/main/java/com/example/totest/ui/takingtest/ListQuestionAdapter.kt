package com.example.totest.ui.takingtest

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.totest.R
import com.example.totest.data.model.QuestionNumber
import com.example.totest.databinding.ItemListQuestionBinding

class ListQuestionAdapter(
    private val listQuestions: MutableList<QuestionNumber>,
    private val listener: OnClickQuestionNumber
) : RecyclerView.Adapter<ListQuestionAdapter.ListQuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListQuestionViewHolder {
        val view =
            ItemListQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListQuestionViewHolder(view)
    }

    override fun getItemCount() = listQuestions.size

    override fun onBindViewHolder(holder: ListQuestionViewHolder, position: Int) =
        holder.bind(listQuestions[position])

    interface OnClickQuestionNumber {
        fun onClickQuestionNumber(position: Int)
    }

    inner class ListQuestionViewHolder(private val binding: ItemListQuestionBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) = listener.onClickQuestionNumber(layoutPosition)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(list: QuestionNumber) = with(binding.tvQuestionNumber) {
            with(list) {
                text = testNumber.toString()
            }
            background = if (list.isQuestionChecked) {
                resources.getDrawable(R.drawable.shape_circle_textview_checked)
            } else resources.getDrawable(R.drawable.shape_circle_textview)
            setOnClickListener(this@ListQuestionViewHolder)
        }
    }
}