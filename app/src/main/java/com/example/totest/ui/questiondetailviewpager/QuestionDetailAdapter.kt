package com.example.totest.ui.questiondetailviewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.totest.data.model.QuestionDetail

class QuestionDetailAdapter(
    fm: FragmentManager,
    private var questionList: ArrayList<QuestionDetail>
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        QuestionDetailFragment.getInstance(position, questionList[position])

    override fun getCount() = questionList.size
}
