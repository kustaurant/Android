package com.kust.kustaurant.data.model.commnity

import com.kust.kustaurant.domain.model.community.PostModification

data class PostResponse(
    val postId: Long,
    val category: String,
    val title: String,
    val body: String,
    val photoUrls: List<String>
)

fun PostResponse.toDomain() : PostModification {
    return PostModification(
        postId = postId,
        category = category,
        title = title,
        body = body,
        photoUrls = photoUrls
    )
}