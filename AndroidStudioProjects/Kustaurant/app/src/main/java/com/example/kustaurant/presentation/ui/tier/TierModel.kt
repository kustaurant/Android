package com.example.kustaurant

data class TierModel(
    val restaurantId: Int,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    var mainTier: Int,
    val partnershipInfo: String,
    var isFavorite : Boolean,
    var isChecked : Boolean,
    var alliance : String,
)
