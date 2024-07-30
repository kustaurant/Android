package com.example.kustaurant.data.model

data class RestaurantResponse(
    val restaurantId: Int,
    val restaurantRanking: Int,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val x: String,
    val y: String,
    val partnershipInfo: String
)