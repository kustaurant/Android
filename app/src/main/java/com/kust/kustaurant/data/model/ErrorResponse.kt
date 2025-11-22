package com.kust.kustaurant.data.model

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String,
    val errors: List<ErrorDetail>?
)

data class ErrorDetail(
    val field: String,
    val value: String,
    val reason: String
)

