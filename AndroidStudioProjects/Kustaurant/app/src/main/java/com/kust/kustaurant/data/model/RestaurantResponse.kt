package com.kust.kustaurant.data.model

data class RestaurantResponse(
    val restaurantId: Int,
    val restaurantRanking: String,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val x: String,
    val y: String,
    val partnershipInfo: String,
    val restaurantScore: String
)
