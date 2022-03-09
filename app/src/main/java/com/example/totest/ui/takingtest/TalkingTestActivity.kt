package com.example.totest.ui.takingtest

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.totest.R
import com.example.totest.data.model.*
import com.example.totest.ui.grammardetail.GrammarDetailFragment
import com.example.totest.ui.listtest.ListTestFragment
import com.example.totest.ui.questiondetailviewpager.QuestionDetailAdapter
import com.example.totest.ui.settings.SettingFragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_talking_test.*
import kotlinx.android.synthetic.main.fragment_notify_dialog.view.*
import kotlinx.android.synthetic.main.fragment_test_result.*

class TalkingTestActivity : AppCompatActivity(), View.OnClickListener {

    private var dataReference: DatabaseReference? = null
    private var grammarList = mutableListOf<GrammarList>()
    private var wordList = mutableListOf<WordList>()
    var questionNumberList = mutableListOf<QuestionNumber>()
    var questionDetailList = arrayListOf<QuestionDetail>()
    var audioList = arrayListOf<Audio>()
    var progressDialog: ProgressDialog? = null
    var mediaPlayer: MediaPlayer? = null
    var score = 0
    var review = false
    var level: Int = 0
    var position: Int = -1
    var isSwitchAnswerOn = false

    companion object {
        const val ARG_LIST_TEST_TITLE = "arg_list_test_title"
        const val ARG_GRAMMAR_LIST = "arg_grammar_list"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        level = intent.getIntExtra(ListTestFragment.ARG_LEVEL, 0)
        Log.i("xxxxxx", "onCreate: ${level}")
        position = intent.getIntExtra(ListTestFragment.ARG_POSITION, -1)
        Log.i("xxxxxx", "onCreate: ${level}")

        setContentView(R.layout.activity_talking_test)
        window.statusBarColor = resources.getColor(R.color.colorBlue)
        progressDialog = ProgressDialog(this)
        initData()
        btnBackToListTest.setOnClickListener(this)
        btnListQuestions.setOnClickListener(this)
    }

    override fun onBackPressed() = when {
        supportFragmentManager.findFragmentById(R.id.frListQuestions) is TestResultFragment -> setResult()
        supportFragmentManager.findFragmentById(R.id.frListQuestions) is GrammarDetailFragment -> finish()
        supportFragmentManager.findFragmentById(R.id.frListQuestions) is SettingFragment -> finish()
        frListQuestions.visibility == View.VISIBLE -> with(frListQuestions) {
            animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_bottom)
            visibility = View.GONE
        }
        else -> initAlertDialog()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnListQuestions -> {
                if (frListQuestions.visibility == View.GONE) {
                    supportFragmentManager.beginTransaction().apply {
                        replace(
                            R.id.frListQuestions,
                            ListQuestionFragment()
                        )
                        addToBackStack(null)
                        commit()
                    }
                    with(frListQuestions) {
                        animation =
                            AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_bottom)
                        visibility = View.VISIBLE
                    }
                } else {
                    with(frListQuestions) {
                        animation = AnimationUtils.loadAnimation(
                            applicationContext,
                            R.anim.slide_out_bottom
                        )
                        visibility = View.GONE
                    }
                }
            }
            R.id.btnBackToListTest -> {
                onBackPressed()
            }
        }
    }

    private fun setResult() {
        setResult(
            Activity.RESULT_OK, Intent()
                .putExtra(TestResultFragment.KEY_TIME, tvDurationTime.text.toString())
                .putExtra(TestResultFragment.KEY_SCORE, score.toString())
                .putExtra(
                    ListTestFragment.ARG_POSITION,
                    intent.getIntExtra(ListTestFragment.ARG_POSITION, 0)
                )
        )
        finish()
    }

    private fun initData() {
        initProgressDialog()
        notifyNetworkStatus()
        FirebaseDatabase.getInstance().apply {

            when (level) {
                R.id.itemPart1 -> {
                    tvLevel.text = getString(R.string.part1)
                    dataReference = getReference("test1").child("part1")
                }
                R.id.itemPart2 -> {
                    tvLevel.text = getString(R.string.part2)
                    dataReference = getReference("test1").child("part2")
                }
                R.id.itemPart3 -> {
                    tvLevel.text = getString(R.string.part3)
                    dataReference = getReference("test1").child("part3")
                }
                R.id.itemPart4 -> {
                    tvLevel.text = getString(R.string.part4)
                    dataReference = getReference("test1").child("part4")
                }
                R.id.itemPart5 -> {
                    tvLevel.text = getString(R.string.part5Basic)
                    dataReference = getReference("test1").child("part5")
                }
                R.id.itemPart6 -> {
                    tvLevel.text = getString(R.string.part6)
                    dataReference = getReference("test1").child("part6")
                }
                R.id.itemPart7 -> {
                    tvLevel.text = getString(R.string.part7)
                    dataReference = getReference("test1").child("part7")
                }
                R.id.itemGrammar -> {
                    grammarList =
                        intent.getParcelableArrayListExtra<GrammarList>(ARG_GRAMMAR_LIST)!!
                    tvLevel.text = grammarList[position].grammarTitle
                    initGrammarDetailFragment()
                }
                R.id.itemWordStudy -> {
                    wordList =
                        intent.getParcelableArrayListExtra<WordList>(ARG_LIST_TEST_TITLE)!!
                    tvLevel.text = wordList[position].testTitle
                    initGrammarDetailFragment()
                }
                else -> {
                    dismissProgressDialog()
                    supportFragmentManager.beginTransaction().apply {
                        replace(
                            R.id.frListQuestions,
                            SettingFragment()
                        )
                        commit()
                    }
                    with(View.GONE) {
                        chronometer.visibility = this
                        btnListQuestions.visibility = this
                    }
                    frListQuestions.visibility = View.VISIBLE
                    tvLevel.text = getString(R.string.settings)
                }
            }
        }

        if (level != R.id.itemGrammar && level != R.id.itemWordStudy) {
            dataReference?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataPractice: DatabaseError) {
                    TODO("Not implemented")
                }

                override fun onDataChange(dataPractice: DataSnapshot) {
                    Log.i("TAG", "onDataChange: $dataPractice")
                    dismissProgressDialog()
                    notifyNetworkStatus()
                    for (i in dataPractice.children) {
                        if (tvLevel.text == getString(R.string.part3)) {
                            val audio = i.getValue(Audio::class.java)
                            audio?.let {
                                audioList.add(it)
                            }
                        } else {
                            val question = i.getValue(QuestionDetail::class.java)
                            question?.let {
                                questionDetailList.add(it)

                            }
                        }
                    }
                    setListQuestionNumber()
                    Log.i("xxxxx", "onDataChange: $questionDetailList")
                    Log.i("xxxxx", "onDataChange: audio $audioList")
                    questionDetailPager?.adapter =
                        QuestionDetailAdapter(supportFragmentManager, questionDetailList, audioList)
                    chronometer.start()
                }
            })
        }
    }

    private fun setListQuestionNumber() {
        if (level == R.id.itemPart3) {
            for (i in 0 until audioList.size * 3) {
                if (level == R.id.itemPart3) {
                    (questionNumberList as ArrayList<QuestionNumber>).add(
                        QuestionNumber(32 + i)
                    )
                }
            }
        } else {
            for (i in 0 until questionDetailList.size) {
                (questionNumberList as ArrayList<QuestionNumber>).add(
                    QuestionNumber(
                        when (level) {
                            R.id.itemPart1 -> 1 + i
                            R.id.itemPart2 -> 7 + i
//                            R.id.itemPart3 -> 32 + i
                            R.id.itemPart4 -> 71 + i
                            R.id.itemPart6 -> 131 + i
                            R.id.itemPart7 -> 147 + i
                            else -> 101 + i
                        }
                    )
                )
            }
        }
    }

    private fun initGrammarDetailFragment() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
            replace(
                R.id.frListQuestions,
                if (level == R.id.itemGrammar) GrammarDetailFragment() else SettingFragment()
            )
            commit()
        }
        frListQuestions.visibility = View.VISIBLE
        with(View.GONE) {
            chronometer.visibility = this
            btnListQuestions.visibility = this
        }
    }

    @SuppressLint("InflateParams")
    private fun initAlertDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_notify_dialog, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView)
        val alertDialog = dialog.show()
        dialogView.btnStay.setOnClickListener { alertDialog.dismiss() }
        dialogView.btnLeave.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
    }

    private fun initProgressDialog() = progressDialog?.apply {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setMessage(getString(R.string.loadingData))
        show()
        setCancelable(false)
    }

    fun dismissProgressDialog() = progressDialog?.dismiss()

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun notifyNetworkStatus() {
        if (!isNetworkAvailable()) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismissProgressDialog()
                Toast.makeText(this, getString(R.string.networkNotification), Toast.LENGTH_LONG)
                    .show()
            }, 5000)
        }
    }
}
