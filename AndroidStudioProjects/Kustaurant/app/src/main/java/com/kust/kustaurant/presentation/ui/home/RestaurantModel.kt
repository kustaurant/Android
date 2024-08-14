package com.kust.kustaurant.presentation.ui.home

data class RestaurantModel(
    val restaurantId: Int, // 아이디
    val restaurantName: String, // 이름
    val restaurantCuisine: String, // 종류
    val restaurantPosition: String, // 위치
    val restaurantImgUrl: String, // 이미지
    val mainTier: Int, // 티어
    val partnershipInfo: String, // 제휴
    val restaurantScore: Double, // 별점
    var isFavorite : Boolean, // 즐겨찾기
    var isChecked : Boolean // 평가여부
)