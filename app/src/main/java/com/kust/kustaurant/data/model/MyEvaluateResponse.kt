package com.kust.kustaurant.data.model

data class MyEvaluateResponse (
    val restaurantId: Int,
    val restaurantName: String,
    val restaurantImgURL: String,
    val cuisine: String,
    val evaluationScore: Double,
    val evaluationBody: String,
    val evaluationItemScores: List<String>?
)