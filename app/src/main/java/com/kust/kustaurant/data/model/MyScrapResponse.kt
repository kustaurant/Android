package com.kust.kustaurant.data.model

data class MyScrapResponse (
    val postId: Int,
    val postCategory: String,
    val postTitle: String,
    val postImgUrl: String?,
    val likeCount: Int,
    val commentCount: Int,
    val timeAgo: String,
    val body: String
)