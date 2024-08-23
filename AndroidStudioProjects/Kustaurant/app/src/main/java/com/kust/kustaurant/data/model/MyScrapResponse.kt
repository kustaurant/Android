package com.kust.kustaurant.data.model

data class MyScrapResponse (
    val postCategory: String,
    val postTitle: String,
    val postBody: String,
    val likeCount: Int,
    val commentCount: Int
)