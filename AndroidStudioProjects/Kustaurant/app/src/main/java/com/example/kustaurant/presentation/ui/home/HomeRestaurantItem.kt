package com.example.kustaurant.presentation.ui.home

data class HomeRestaurantItem(
    val restaurantId: Int, // 아이디
    val restaurantName: String, // 이름
    val restaurantCuisine: String, // 종류
    val restaurantPosition: String, // 위치
    val restaurantImgUrl: String, // 이미지
    val mainTier: Int, // 티어
    val partnershipInfo: String // 제휴
)