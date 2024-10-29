package com.kust.kustaurant.data.model

data class DrawRestaurantData(
    val restaurantId: Int,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val isEvaluated: Boolean?,
    val isFavorite: Boolean?,
    val x: String,
    val y: String,
    val partnershipInfo: String?,
    val restaurantScore: Double?,
    val restaurantAddress: String,
    val restaurantTel: String,
    val restaurantUrl: String,
    val restaurantVisitCount: Int,
    val restaurantEvaluationCount: Int,
    val status: String,
    val mainScoreMaxTen: String
)