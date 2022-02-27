package com.example.totest.ui.takingtest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.totest.R
import com.example.totest.data.model.TestList
import com.example.totest.databinding.FragmentTestResultBinding
import com.example.totest.ui.listtest.ListTestFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_talking_test.*
import kotlinx.android.synthetic.main.fragment_test_result.*

class TestResultFragment : Fragment(), View.OnClickListener {

    private var position = -1
    private var level = 0
    private var binding: FragmentTestResultBinding? = null

    companion object {
        const val KEY_TIME = "key_time"
        const val KEY_SCORE = "key_score"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        (activity as TalkingTestActivity).apply {
            level = intent.getIntExtra(ListTestFragment.ARG_LEVEL, 0)
        }
        binding = FragmentTestResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnReview.setOnClickListener(this)
        btnExit.setOnClickListener(this)
        (activity as TalkingTestActivity).apply {
            tvDurationTime.text = chronometer.text.toString()
            tvCorrectAnswer.text = StringBuilder().append(score.toString())
                .append("/${questionDetailList.size}")
        }
        setTimeAndScore()
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnReview -> {
                (activity as TalkingTestActivity).apply {
                    review = true
                    btnListQuestions.visibility = View.VISIBLE
                }
                activity?.apply {
                    frListQuestions?.visibility = View.GONE
                    questionDetailPager.apply {
                        adapter?.notifyDataSetChanged()
                        currentItem = 0
                    }
                }
            }
            R.id.btnExit -> {
                activity?.apply {
                    setResult(
                        Activity.RESULT_OK, Intent()
                            .putExtra(KEY_TIME, tvDurationTime.text.toString())
                            .putExtra(KEY_SCORE, (activity as TalkingTestActivity).score.toString())
                            .putExtra(
                                ListTestFragment.ARG_POSITION,
                                activity?.intent?.getIntExtra(ListTestFragment.ARG_POSITION, -1)
                            )
                    )
                    finish()
                }
            }
        }
    }

    private fun setTimeAndScore() {
        val preferences =
            activity?.getSharedPreferences(getString(R.string.fileName), Context.MODE_PRIVATE)
        activity?.intent?.apply {
            position = getIntExtra(ListTestFragment.ARG_POSITION, -1)
        }
        val dataTimeAndScore = preferences?.getString("$level", "")
        val gson = GsonBuilder().setPrettyPrinting().create()
        val listTimeandScore =
            gson.fromJson(dataTimeAndScore, Array<TestList>::class.java)?.toList()?.toMutableList()
                ?: arrayListOf()
        listTimeandScore.add(
            TestList(
                "${getString(R.string.practice)} ${position + 1}",
                (activity as TalkingTestActivity).chronometer.text.toString(),
                (activity as TalkingTestActivity).score.toString()
            )
        )
        val json = Gson().toJson(listTimeandScore)
        preferences?.edit()?.apply {
            putString("$level", json)
            apply()
        }
    }
}
