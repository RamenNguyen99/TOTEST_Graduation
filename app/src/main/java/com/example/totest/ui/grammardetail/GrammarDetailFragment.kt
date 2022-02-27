package com.example.totest.ui.grammardetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.totest.R
import com.example.totest.data.model.GrammarDetail
import com.example.totest.data.model.ToeicIntro
import com.example.totest.databinding.FragmentGrammarDetailBinding
import com.example.totest.ui.listtest.ListTestActivity
import com.example.totest.ui.listtest.ListTestFragment
import com.example.totest.ui.takingtest.TalkingTestActivity
import com.google.firebase.database.*

class GrammarDetailFragment : Fragment() {

    private var binding: FragmentGrammarDetailBinding? = null
    private var grammarDetailAdapter: GrammarDetailAdapter? = null
    private var toeicIntroAdapter: ToeicIntroAdapter? = null
    private var grammarDetailList = mutableListOf<GrammarDetail>()
    private var toeicIntroList = mutableListOf<ToeicIntro>()
    private var databaseReference: DatabaseReference? = null
    private var level: Int? = null

    companion object {

        fun getInstance(level: Int): GrammarDetailFragment =
            GrammarDetailFragment().apply {
                val bundle = Bundle().apply {
                    putInt(ListTestFragment.ARG_LEVEL, level)
                }
                arguments = bundle
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        level = arguments?.getInt(ListTestFragment.ARG_LEVEL)
        binding = FragmentGrammarDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
    }

    private fun initRecyclerView() = binding?.recycleViewGrammarDetail?.apply {
        layoutManager = LinearLayoutManager(activity)
        grammarDetailAdapter = GrammarDetailAdapter(grammarDetailList)
        toeicIntroAdapter = ToeicIntroAdapter(toeicIntroList)
        adapter =
            if (level == R.id.itemToeicIntroduction) toeicIntroAdapter else grammarDetailAdapter
    }

    private fun initData() {
        Log.i("xxxxx", "initData: ${FirebaseDatabase.getInstance().getReference("toeicIntroduction")}")
        val position = activity?.intent?.getIntExtra(ListTestFragment.ARG_POSITION, 0)
        databaseReference = when (level) {
            R.id.itemToeicIntroduction -> {
                (activity as ListTestActivity).apply {
                    initProgressDialog()
                    //notifyNetworkStatus()
                }
                FirebaseDatabase.getInstance().getReference("toeicIntroduction")
            }
            else -> {
                (activity as TalkingTestActivity).notifyNetworkStatus()
                FirebaseDatabase.getInstance().getReference("grammarDetail0${position?.plus(1)}")
            }
        }
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not implemented")
            }

            override fun onDataChange(grammarDetailData: DataSnapshot) {
                when (level) {
                    R.id.itemToeicIntroduction -> {
                        (activity as ListTestActivity).dismissProgressDialog()
                        for (i in grammarDetailData.children) {
                            val introDetail = i.getValue(ToeicIntro::class.java)
                            introDetail?.let {
                                toeicIntroList.add(it)
                            }
                        }
                        toeicIntroAdapter?.notifyDataSetChanged()
                    }
                    else -> {
                        (activity as TalkingTestActivity).dismissProgressDialog()
                        for (i in grammarDetailData.children) {
                            val introDetail = i.getValue(GrammarDetail::class.java)
                            introDetail?.let {
                                grammarDetailList.add(it)
                            }
                        }
                        grammarDetailAdapter?.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
