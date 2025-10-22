package com.kust.kustaurant.data.model.commnity

data class CommunityCommentReactionResponse(
    val likeCount : Int,
    val dislikeCount : Int,
    val reactionType : String?,
)