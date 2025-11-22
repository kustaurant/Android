package com.kust.kustaurant.data.model

data class MyProfileResponse (
    val nickname: String,
    val savedRestaurantCnt: Int = 0,
    val evalCnt: Int = 0,
    val postCnt: Int = 0,
    val postCommentCnt: Int = 0,
    val savedPostCnt: Int = 0,
    val email: String,
    val phoneNumber: String,
    val iconUrl: String = ""
)

data class MyProfileRequest (
    val nickname: String,
    val email: String,
    val phoneNumber: String
)