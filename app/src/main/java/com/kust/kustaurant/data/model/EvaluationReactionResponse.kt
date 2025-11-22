package com.kust.kustaurant.data.model

data class EvaluationReactionResponse(
    val evaluationId: Int,
    val reaction: String?, // "LIKE", "DISLIKE", null
    val likeCount: Int,
    val dislikeCount: Int
)

