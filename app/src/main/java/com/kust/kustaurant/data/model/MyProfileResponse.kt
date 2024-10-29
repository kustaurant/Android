package com.kust.kustaurant.data.model

data class MyProfileResponse (
    var iconImageUrl : String = "",
    var nickname: String = "",
    var email: String = "",
    var phoneNumber: String = ""
)

data class MyProfileRequest (
    val nickname: String,
    val email: String,
    val phoneNumber: String
)