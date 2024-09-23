package com.kust.kustaurant.data.model

data class MyFavoriteResponse (
    val restaurantName: String,
    val restaurantId : Int,
    val restaurantImgURL: String,
    val mainTier: Int,
    val restaurantType: String,
    val restaurantPosition: String
)