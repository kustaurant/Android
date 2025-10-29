package com.kust.kustaurant.data.model.commnity

import com.kust.kustaurant.domain.model.community.CommunityPost

data class CommunityPostResponse(
    val postId: Long,
    val category: PostCategoryDto ,
    val title: String,
    val body: String,
    val photoUrls: List<String>?,

    val writeruserId: Long,
    val writernickname: String,
    val writerevalCount: Long,
    val writericonUrl: String?,

    val timeAgo: String?,
    val createdAt: String,
    val updatedAt: String?,

    val likeOnlyCount: Long,
    val dislikeOnlyCount: Long,
    val totalLikes: Long,
    val commentCount: Long,
    val scrapCount: Long,
    val visitCount: Long,

    val myReaction: String?,
    val isScrapped: Boolean,
    val isPostMine: Boolean,

    val comments: List<CommunityPostCommentResponse>?
)

fun CommunityPostResponse.toDomain(): CommunityPost =
    CommunityPost(
        postId = postId,
        category = category.toDomain(),
        title = title,
        body = body,
        photoUrls = photoUrls,
        writeruserId = writeruserId,
        writernickname = writernickname,
        writerevalCount = writerevalCount,
        writericonUrl = writericonUrl,
        timeAgo = timeAgo,
        createdAt = createdAt,
        updatedAt = updatedAt,
        likeOnlyCount = likeOnlyCount,
        dislikeOnlyCount = dislikeOnlyCount,
        totalLikes = totalLikes,
        commentCount = commentCount,
        scrapCount = scrapCount,
        visitCount = visitCount,
        myReaction = myReaction,
        isScrapped = isScrapped,
        isPostMine = isPostMine,
        comments = comments?.map { it.toDomain() }.orEmpty()
    )