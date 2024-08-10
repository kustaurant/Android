package com.example.kustaurant.presentation.ui.mypage

data class SaveRestaurantData (
    val restaurantName: String,
    val restaurantImgURL: String,
    val mainTier: Int,
    val restaurantType: String,
    val restaurantPosition: String
)