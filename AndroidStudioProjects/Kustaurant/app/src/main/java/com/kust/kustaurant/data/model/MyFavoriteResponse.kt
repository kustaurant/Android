package com.kust.kustaurant.data.model

data class MyFavoriteResponse (
    val restaurantName: String,
    val restaurantImgURL: String,
    val mainTier: Int,
    val restaurantType: String,
    val restaurantPosition: String
)