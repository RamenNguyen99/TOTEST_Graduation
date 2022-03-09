package com.example.totest.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Audio(
    var audioSumaries: String = "",
    var image: String = "",
    var question1: QuestionDetail? = null,
    var question2: QuestionDetail?= null,
    var question3: QuestionDetail? = null
): Parcelable
