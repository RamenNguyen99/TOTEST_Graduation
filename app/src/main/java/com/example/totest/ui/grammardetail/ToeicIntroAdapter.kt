package com.example.totest.ui.grammardetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.totest.data.model.ToeicIntro
import com.example.totest.databinding.ItemGrammarDetailBinding

class ToeicIntroAdapter(private val toeicIntro: MutableList<ToeicIntro>) :
    RecyclerView.Adapter<ToeicIntroAdapter.ToeicIntroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ToeicIntroViewHolder {
        val view =
            ItemGrammarDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToeicIntroViewHolder(view)
    }

    override fun getItemCount() = toeicIntro.size

    override fun onBindViewHolder(holder: ToeicIntroViewHolder, position: Int) =
        holder.bindView(toeicIntro[position])

    inner class ToeicIntroViewHolder(private var binding: ItemGrammarDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(toeicIntro: ToeicIntro) = with(itemView) {
            with(toeicIntro) {
                binding.run {
                    tvGrammarDetailTitle.text = introTitle
                    tvGrammarDetailDescription.text = introContent
                }
            }
        }
    }
}
