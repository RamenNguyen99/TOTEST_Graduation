package com.example.totest.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToeicIntro(
    var introTitle: String = "",
    var introContent: String = ""
) : Parcelable
