package com.example.totest.ui.questiondetailviewpager

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.totest.R
import com.example.totest.data.model.QuestionDetail
import com.example.totest.databinding.FragmentQuestionDetailBinding
import com.example.totest.ui.listtest.ListTestFragment
import com.example.totest.ui.takingtest.TalkingTestActivity
import kotlinx.android.synthetic.main.activity_talking_test.*
import kotlinx.android.synthetic.main.fragment_question_detail.*
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class QuestionDetailFragment : Fragment(), View.OnClickListener {

    private var data: QuestionDetail? = null
    private var position = -1
    private var level: Int? = null
    private var isDestroy = false
    private var binding: FragmentQuestionDetailBinding? = null

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_DATA = "arg_data"

        fun getInstance(position: Int, question: QuestionDetail): QuestionDetailFragment =
            QuestionDetailFragment().apply {
                val bundle = Bundle().apply {
                    putInt(ARG_POSITION, position)
                    putParcelable(ARG_DATA, question)
                }
                arguments = bundle
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            Log.i("xxxx", "onCreateView: ${position}")
            data = it.getParcelable(ARG_DATA)
        }
        (activity as TalkingTestActivity).apply {
            progressDialog?.dismiss()
        }
        level = activity?.intent?.getIntExtra(ListTestFragment.ARG_LEVEL, 0)
        binding = FragmentQuestionDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as TalkingTestActivity).apply {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        showView()
        val switchState = activity?.getSharedPreferences("switchcase", Context.MODE_PRIVATE)
        val isSwitchAnswer = switchState?.getBoolean("switchState", false)
        if (isSwitchAnswer == true) {
            onClickOptions()
        } else
            setValueForMyAnswer()
        onClickPlayAudio()
        setDataFirebase()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rbAnswerA -> {
                data?.myAnswer =
                    (activity as TalkingTestActivity).questionDetailList[(activity as TalkingTestActivity).questionDetailPager.currentItem].answerA
                showAnswerAfterOnClick()
            }
            R.id.rbAnswerB -> {
                data?.myAnswer =
                    (activity as TalkingTestActivity).questionDetailList[(activity as TalkingTestActivity).questionDetailPager.currentItem].answerB
                showAnswerAfterOnClick()
            }
            R.id.rbAnswerC -> {
                data?.myAnswer =
                    (activity as TalkingTestActivity).questionDetailList[(activity as TalkingTestActivity).questionDetailPager.currentItem].answerC
                showAnswerAfterOnClick()
            }
            R.id.rbAnswerD -> {
                data?.myAnswer =
                    (activity as TalkingTestActivity).questionDetailList[(activity as TalkingTestActivity).questionDetailPager.currentItem].answerD
                showAnswerAfterOnClick()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser && isResumed) {
            (activity as TalkingTestActivity).mediaPlayer?.pause()
            imgState.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as TalkingTestActivity).mediaPlayer?.stop()
        isDestroy = true
    }

    private fun showView() {
        when (level) {
            R.id.itemPart1 -> {
                with(View.VISIBLE) {
                    tvQuestionContent.visibility = this
                    imgQuestionTitle.visibility = this
                    cardViewAudio.visibility = this
                }
                tvQuestionTitle.visibility = View.GONE
                setLayoutHeight()
            }
            R.id.itemPart2 -> {
                with(View.VISIBLE) {
                    tvQuestionContent.visibility = this
                    cardViewAudio.visibility = this
                }
                with(View.GONE) {
                    tvQuestionTitle.visibility = this
                    rbAnswerD.visibility = this
                    divider4.visibility = this
                }
                setLayoutHeight()
            }
            R.id.itemPart3, R.id.itemPart4 -> {
                with(View.VISIBLE) {
                    tvQuestionContent.visibility = this
                    cardViewAudio.visibility = this
                }
                with(View.GONE) {
                    tvQuestionTitle.visibility = this
                }
                setLayoutHeight()
            }
            R.id.itemPart6, R.id.itemPart7 -> {
                tvQuestionContent.visibility = View.VISIBLE
                tvQuestionTitle.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                if (level == R.id.itemPart7) {
                    setLayoutHeight()
                }
            }
        }
    }

    private fun setDataFirebase() = data?.let {
        with(it) {
            when (level) {
                R.id.itemPart1 -> Glide.with(activity as TalkingTestActivity).load(questionTitle)
                    .into(
                        imgQuestionTitle
                    )
            }
            tvQuestionContent.text = question
            if (level != R.id.itemPart1 && level != R.id.itemPart2) {
                tvQuestionTitle.text = questionTitle
                tvQuestionContent.text = question
                rbAnswerA.text = answerA
                rbAnswerB.text = answerB
                rbAnswerC.text = answerC
                rbAnswerD.text = answerD
                tvExplanation.text = explanation
                tvTranslation.text = translation
            }
        }
        if ((activity as TalkingTestActivity).review) {
            showAnswerAfterOnClick()
        }
    }

    private fun onClickPlayAudio() {
        @SuppressLint("SimpleDateFormat")
        val timeFormat = SimpleDateFormat("mm:ss")
        imgState.setOnClickListener {
            (activity as TalkingTestActivity).mediaPlayer?.apply {
                try {
                    setDataSource(data?.audio)
                    setOnPreparedListener { mp -> mp.start() }
                    prepare()
                } catch (e: Exception) {
                }
                seekBarPlay.max = duration
                tvTotalTime.text = timeFormat.format(duration)
                (activity as TalkingTestActivity).runOnUiThread(object : Runnable {
                    override fun run() {
                        if (isDestroy) {
                            return
                        }
                        try {
                            (activity as TalkingTestActivity).mediaPlayer?.currentPosition?.apply {
                                seekBarPlay.progress = this
                                tvCurrentTime.text = timeFormat.format(this)
                            }
                            Handler().postDelayed(this, 1000)
                        } catch (e: Exception) {
                        }
                    }
                })
                seekBarChangeListener()
                if (isPlaying) {
                    pause()
                    imgState.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                } else {
                    start()
                    imgState.setImageResource(R.drawable.ic_pause_black_24dp)
                }
            }
        }
    }

    private fun setValueForMyAnswer() = rgAnswer.setOnCheckedChangeListener { _, _ ->
        (activity as TalkingTestActivity).questionNumberList[(activity as TalkingTestActivity).questionDetailPager.currentItem].apply {
            data?.apply {
                when {
                    rbAnswerA.isChecked -> {
                        myAnswer = answerA
                        isQuestionChecked = true
                    }
                    rbAnswerB.isChecked -> {
                        myAnswer = answerB
                        isQuestionChecked = true
                    }
                    rbAnswerC.isChecked -> {
                        myAnswer = answerC
                        isQuestionChecked = true
                    }
                    rbAnswerD.isChecked -> {
                        myAnswer = answerD
                        isQuestionChecked = true
                    }
                }
            }
        }
    }

    private fun onClickOptions() {
        rbAnswerA.setOnClickListener(this)
        rbAnswerB.setOnClickListener(this)
        rbAnswerC.setOnClickListener(this)
        rbAnswerD.setOnClickListener(this)
    }

    private fun showAnswerAfterOnClick() {
        (activity as TalkingTestActivity).questionNumberList[(activity as TalkingTestActivity).questionDetailPager.currentItem].apply {
            data?.apply {
                isQuestionChecked = true
                if (level == R.id.itemPart1 || level == R.id.itemPart2) {
                    rbAnswerA.text = answerA
                    rbAnswerB.text = answerB
                    rbAnswerC.text = answerC
                    rbAnswerD.text = answerD
                    tvQuestionContent.text =
                        if (level == R.id.itemPart2) question else questionContent
                } else {
                    cardViewExplanation.visibility = View.VISIBLE
                }
                with(this) {
                    if (myAnswer != correctAnswer) {
                        when (correctAnswer) {
                            answerA -> rbAnswerA.setBackgroundColor(
                                if (myAnswer.isBlank()) resources.getColor(R.color.colorOrange) else resources.getColor(
                                    R.color.colorBlueAnswer
                                )
                            )
                            answerB -> rbAnswerB.setBackgroundColor(
                                if (myAnswer.isBlank()) resources.getColor(R.color.colorOrange) else resources.getColor(
                                    R.color.colorBlueAnswer
                                )
                            )
                            answerC -> rbAnswerC.setBackgroundColor(
                                if (myAnswer.isBlank()) resources.getColor(R.color.colorOrange) else resources.getColor(
                                    R.color.colorBlueAnswer
                                )
                            )
                            answerD -> rbAnswerD.setBackgroundColor(
                                if (myAnswer.isBlank()) resources.getColor(R.color.colorOrange) else resources.getColor(
                                    R.color.colorBlueAnswer
                                )
                            )
                        }
                        when (myAnswer) {
                            answerA -> rbAnswerA.setBackgroundColor(resources.getColor(R.color.colorPink))
                            answerB -> rbAnswerB.setBackgroundColor(resources.getColor(R.color.colorPink))
                            answerC -> rbAnswerC.setBackgroundColor(resources.getColor(R.color.colorPink))
                            answerD -> rbAnswerD.setBackgroundColor(resources.getColor(R.color.colorPink))
                        }
                    }
                }
                with(false) {
                    rbAnswerA.isClickable = this
                    rbAnswerB.isClickable = this
                    rbAnswerC.isClickable = this
                    rbAnswerD.isClickable = this
                }
            }
        }
    }

    private fun seekBarChangeListener() =
        seekBarPlay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    (activity as TalkingTestActivity).mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    private fun setLayoutHeight() = with(ViewGroup.LayoutParams.WRAP_CONTENT) {
        rbAnswerA.layoutParams.height = this
        rbAnswerB.layoutParams.height = this
        rbAnswerC.layoutParams.height = this
        rbAnswerD.layoutParams.height = this
    }
}
