package com.kust.kustaurant.data.model

data class DrawRestaurantData(
    val restaurantId: Int,
    val restaurantName: String,
    val restaurantMenu: String,
    val restaurantPosition: String,
    val restaurantAddress: String,
    val restaurantTel: String,
    val restaurantUrl: String,
    val restaurantImgUrl: String,
    val restaurantVisitCount: Int,
    val visitCount: Int,
    val restaurantEvaluationCount: Int,
    val restaurantScoreSum: Double,
    val mainTier: Int,
    val restaurantCuisine: String,
    val restaurantLatitude: String,
    val restaurantLongitude: String,
    val status: String,
    val mainScoreMaxTen: String
)
