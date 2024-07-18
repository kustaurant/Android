package com.example.kustaurant.presentation.ui.tier

data class TierModel(
    val restaurantId: Int,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    var mainTier: Int,
    val partnershipInfo: String,
    var isFavorite : Boolean,
    var isChecked : Boolean
)
