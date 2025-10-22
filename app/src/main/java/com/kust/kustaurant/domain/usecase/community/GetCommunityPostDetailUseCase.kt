package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.model.community.CommunityPostComment
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityPostDetailUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Long): CommunityPost {
        val post = communityRepository.getPostDetail(postId)

        val safeComments = post.comments?.map { comment ->
            normalizeComment(comment)
        } ?: emptyList()

        return post.copy(comments = safeComments)
    }

    private fun normalizeComment(comment: CommunityPostComment): CommunityPostComment {
        val safeReplies = comment.replies.map { normalizeComment(it) }
        return comment.copy(replies = safeReplies)
    }
}

