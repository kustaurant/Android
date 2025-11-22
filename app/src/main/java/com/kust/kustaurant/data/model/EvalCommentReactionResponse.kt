package com.kust.kustaurant.data.model

data class EvalCommentReactionResponse(
    val evalCommentId: Int,
    val reaction: String?, // "LIKE", "DISLIKE", null (반응 해제)
    val likeCount: Int,
    val dislikeCount: Int
)

