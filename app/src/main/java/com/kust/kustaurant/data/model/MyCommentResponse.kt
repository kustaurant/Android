package com.kust.kustaurant.data.model

data class MyCommentResponse (
    val postId: Int,
    val postCategory: String,
    val postTitle: String,
    val postcommentBody: String,
    val commentlikeCount: Int,
)