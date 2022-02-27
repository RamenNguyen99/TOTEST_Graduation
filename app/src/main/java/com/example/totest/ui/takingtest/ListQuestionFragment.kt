package com.example.totest.ui.takingtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.totest.R
import com.example.totest.databinding.FragmentListQuestionBinding
import com.example.totest.ui.listtest.ListTestFragment
import kotlinx.android.synthetic.main.activity_talking_test.*

class ListQuestionFragment : Fragment(), ListQuestionAdapter.OnClickQuestionNumber {

    private var binding: FragmentListQuestionBinding? = null
    private var level: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListQuestionBinding.inflate(inflater, container, false)
        level = activity?.intent?.getIntExtra(ListTestFragment.ARG_LEVEL, 0)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        onClickSubmit()
        if ((activity as TalkingTestActivity).review) {
            binding?.btnSubmit?.visibility = View.GONE
        }
    }

    override fun onClickQuestionNumber(position: Int) {
        (activity as? TalkingTestActivity).apply {
            frListQuestions?.visibility = View.GONE
            questionDetailPager?.currentItem = position
        }
    }

    private fun initRecycleView() = binding?.recycleViewListQuestions?.apply {
        setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity, 5)
        adapter = (activity as? TalkingTestActivity)?.questionNumberList?.let {
            ListQuestionAdapter(
                it, this@ListQuestionFragment
            )
        }
    }

    private fun onClickSubmit() = binding?.btnSubmit?.setOnClickListener {
        parentFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
            replace(R.id.frListQuestions, TestResultFragment())
            commit()
        }

        (activity as? TalkingTestActivity)?.apply {
            chronometer?.stop()
            with(View.GONE) {
                chronometer?.visibility = this
                btnListQuestions?.visibility = this
            }
            questionDetailList.forEach { listQuestionDetailItem ->
                if (listQuestionDetailItem.correctAnswer == listQuestionDetailItem.myAnswer) {
                    score += 1
                }
            }
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null
        }
    }
}