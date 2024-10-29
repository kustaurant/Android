package com.kust.kustaurant.data.model

data class NaverLoginRequest(
    val provider: String,
    val providerId: String,
    val naverAccessToken: String
)