package com.example.totest.ui.listtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.totest.data.model.TestList
import com.example.totest.databinding.ItemListTestBinding

class TestListAdapter(
    private val listTests: MutableList<TestList>,
    private val listener: OnClickTestItem
) :
    RecyclerView.Adapter<TestListAdapter.ListReadingTestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListReadingTestViewHolder {
        val view = ItemListTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListReadingTestViewHolder(view)
    }

    override fun getItemCount() = listTests.size

    override fun onBindViewHolder(holder: ListReadingTestViewHolder, position: Int) =
        holder.bindView(listTests[position])

    interface OnClickTestItem {
        fun onClickTestItem(position: Int)
    }

    inner class ListReadingTestViewHolder(private val binding: ItemListTestBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        override fun onClick(v: View?) = listener.onClickTestItem(layoutPosition)

        fun bindView(listItems: TestList) {
            with(itemView) {
                with(listItems) {
                    binding.run {
                        tvTestName.text = testNumber
                        tvTimeDisplay.text = timeDisplay
                        tvScoreDisplay.text = scoreDisplay
                    }
                }
                binding.clPractice.setOnClickListener(this@ListReadingTestViewHolder)
            }
        }
    }
}
