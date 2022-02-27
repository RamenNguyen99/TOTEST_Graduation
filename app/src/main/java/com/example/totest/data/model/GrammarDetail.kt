package com.example.totest.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GrammarDetail(
    var grammarDetailTitle: String = "",
    var grammarDetailDescription: String = ""
) : Parcelable
