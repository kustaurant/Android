package com.kust.kustaurant.domain.model

data class ServiceDownEvent(
    val seq: Long,
    val code: String?,
    val message: String?
)