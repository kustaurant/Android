package com.kust.kustaurant.data.model

data class LoginResponse(
     val accessToken: String,
     val refreshToken: String
)

fun LoginResponse.toDomain() = com.kust.kustaurant.domain.model.auth.AuthToken(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)