package com.example.totest.data.model


data class Audio(
    var audioSumaries: String = "",
    var image: String = "",
    var question1: QuestionDetail,
    var question2: QuestionDetail,
    var question3: QuestionDetail
)