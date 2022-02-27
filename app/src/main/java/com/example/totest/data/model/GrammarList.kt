package com.example.totest.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GrammarList (
    var grammarTitle: String = "",
    var grammarExample: String = ""
) : Parcelable
