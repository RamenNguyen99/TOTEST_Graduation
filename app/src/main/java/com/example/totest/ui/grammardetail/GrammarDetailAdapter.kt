package com.example.totest.ui.grammardetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.totest.data.model.GrammarDetail
import com.example.totest.databinding.ItemGrammarDetailBinding

class GrammarDetailAdapter(private val grammarDetail: MutableList<GrammarDetail>) :
    RecyclerView.Adapter<GrammarDetailAdapter.GrammarDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): GrammarDetailViewHolder {
        val view =
            ItemGrammarDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GrammarDetailViewHolder(view)
    }

    override fun getItemCount() = grammarDetail.size

    override fun onBindViewHolder(holder: GrammarDetailViewHolder, position: Int) =
        holder.bindView(grammarDetail[position])

    inner class GrammarDetailViewHolder(private var binding: ItemGrammarDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(grammarDetail: GrammarDetail) = with(itemView) {
            with(grammarDetail) {
                binding.run {
                    tvGrammarDetailTitle.text = grammarDetailTitle
                    tvGrammarDetailDescription.text = grammarDetailDescription
                }
            }
        }
    }
}
