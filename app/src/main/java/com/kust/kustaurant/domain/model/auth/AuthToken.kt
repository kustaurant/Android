package com.kust.kustaurant.domain.model.auth

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)