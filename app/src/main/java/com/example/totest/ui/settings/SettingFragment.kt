package com.example.totest.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.totest.databinding.FragmentSettingBinding
import com.example.totest.ui.takingtest.TalkingTestActivity
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {
    private var binding: FragmentSettingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwitchState()
    }

    private fun setSwitchState() {
        val switchState = activity?.getSharedPreferences("switchcase", Context.MODE_PRIVATE)
        val isSwitchChecked = switchState?.getBoolean("switchState", false)
        if (isSwitchChecked == true) {
            switchShowAnswer.isChecked = true
        }
        switchShowAnswer.setOnCheckedChangeListener { _, isChecked ->
            (activity as TalkingTestActivity).isSwitchAnswerOn = isChecked
            switchState?.edit()
                ?.putBoolean("switchState", (activity as TalkingTestActivity).isSwitchAnswerOn)
                ?.apply()
        }
    }
}