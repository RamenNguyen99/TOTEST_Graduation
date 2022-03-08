package com.example.totest.ui.questiondetailviewpager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.totest.data.model.Audio
import com.example.totest.data.model.QuestionDetail

class QuestionDetailAdapter(
    fm: FragmentManager,
    private var questionList: ArrayList<QuestionDetail>,
    private var audioList: ArrayList<Audio>
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
//        Log.i("getItem", "getItem: ${audioList[position]}")
        if (questionList.size == 0){
            Log.i("getItem", "getItem: ${audioList[position]}")
            return QuestionDetailFragment.getInstance(position,null,audio = audioList[position])
        }
        return QuestionDetailFragment.getInstance(position, questionList[position], null)
    }


    override fun getCount() : Int {
        if (questionList.size == 0){
            return audioList.size
        }
        return questionList.size
    }
}
