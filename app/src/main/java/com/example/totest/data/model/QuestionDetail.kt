package com.example.totest.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionDetail(
    val questionTitle: String = "",
    val optionA: String = "",
    val optionB: String = "",
    val optionC: String = "",
    val optionD: String = "",
    val correctAnswer: String = "",
    var myAnswer: String = "",
    val audio: String = "",
    val explanation: String = "",
    val translation: String = "",
    val questionContent: String = "",
    val question: String = "",
    val image: String = "",
    val image2: String = "",
    val image3: String = ""
) : Parcelable
