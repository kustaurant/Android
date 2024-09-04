package com.kust.kustaurant.data.model

data class CommentLikeResponse(
    val commentLikeStatus: Int,
    val commentLikeCount: Int,
    val commentDislikeCount: Int
)
