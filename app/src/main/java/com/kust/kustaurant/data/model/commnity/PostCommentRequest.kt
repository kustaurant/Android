package com.kust.kustaurant.data.model.commnity

data class PostCommentRequest(
    val content : String,
    val parentCommentId : Long?,
) {
    init{
        require(content.length in 1..1000) { "Content must be between 1 and 1000 characters." }
    }
}
