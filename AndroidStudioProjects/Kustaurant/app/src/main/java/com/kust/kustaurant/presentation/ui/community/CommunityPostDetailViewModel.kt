package com.kust.kustaurant.presentation.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.data.model.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.CommunityPostScrapResponse
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityPostComment
import com.kust.kustaurant.domain.usecase.community.DeleteCommunityCommentUseCase
import com.kust.kustaurant.domain.usecase.community.DeleteCommunityPostUseCase
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostDetailUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostCommentReactUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostCommentReplyUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostDetailScrapUseCase
import com.kust.kustaurant.domain.usecase.community.PostPostScrapLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityPostDetailViewModel @Inject constructor(
    private val getCommunityPostDetail: GetCommunityPostDetailUseCase,
    private val postPostScrap: PostCommunityPostDetailScrapUseCase,
    private val postPostLikeUseCase: PostPostScrapLikeUseCase,
    private val postCreateCommentReplyUseCase: PostCommunityPostCommentReplyUseCase,
    private val postCommentReactUseCase: PostCommunityPostCommentReactUseCase,
    private val deletePostUseCase: DeleteCommunityPostUseCase,
    private val deleteCommentUseCase: DeleteCommunityCommentUseCase,


    //private val postpostImageUploadUseCase
    //private val patchPostModify
) : ViewModel() {
    private val _communityPostDetail = MutableLiveData<CommunityPost>()
    val communityPostDetail: LiveData<CommunityPost> = _communityPostDetail

    private val _postLike = MutableLiveData<Boolean>(true)
    val postLike: LiveData<Boolean> = _postLike

    private val _isPostScrap = MutableLiveData<CommunityPostScrapResponse>()
    val isPostScrap: LiveData<CommunityPostScrapResponse> = _isPostScrap

    private val _isPostLike = MutableLiveData<CommunityPostLikeResponse>()
    val isPostLike: LiveData<CommunityPostLikeResponse> = _isPostLike

    private val _commentReply = MutableLiveData<CommunityPostComment>()
    val commentReply: LiveData<CommunityPostComment> = _commentReply

    private val _commentReact = MutableLiveData<Pair<Int, CommunityPostCommentReactResponse>>()
    val commentReact: LiveData<Pair<Int, CommunityPostCommentReactResponse>> get() = _commentReact
    fun loadCommunityPostDetail(postId: Int) {
        viewModelScope.launch {
            try {
                _communityPostDetail.value = getCommunityPostDetail(postId)
            } catch (e: Exception) {
                Log.e(
                    "CommunityPostDetailViewModel",
                    "From loadCommunityPostDetail, Error msg is $e"
                )
            }
        }
    }

    fun postPostDetailScrap(postId: Int) {
        viewModelScope.launch {
            try {
                _isPostScrap.value = postPostScrap(postId)
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From postPostDetailScrap, Error msg is $e")
            }
        }
    }

    fun postPostLike(postId: Int) {
        viewModelScope.launch {
            try {
                _isPostLike.value = postPostLikeUseCase(postId)
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From postPostLike, Error msg is $e")
            }
        }
    }

    fun postCreateCommentReply(content: String, postId: String, parentCommentId: String) {
        viewModelScope.launch {
            try {
                _commentReply.value =
                    postCreateCommentReplyUseCase(content, postId, parentCommentId)
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From postCommentReply, Error msg is $e")
            }
        }
    }


    fun postCommentReact(commentId: Int, action: String) {
        viewModelScope.launch {
            try {
                val response = postCommentReactUseCase(commentId, action)

                _communityPostDetail.value?.let { postDetail ->
                    val updatedComments = postDetail.postCommentList.toMutableList()
                    var updatedPosition: Int? = null

                    for ((index, comment) in updatedComments.withIndex()) {
                        // 댓글 순회
                        if (comment.commentId == commentId) {
                            updatedComments[index] = comment.copy(
                                likeCount = response.likeCount,
                                dislikeCount = response.dislikeCount,
                                isLiked = response.commentLikeStatus == 1,
                                isDisliked = response.commentLikeStatus == -1
                            )
                            updatedPosition = index
                            break
                        }

                        // 대댓글 순회
                        val updatedReplies = comment.repliesList.toMutableList()
                        for ((replyIndex, reply) in updatedReplies.withIndex()) {
                            if (reply.commentId == commentId) {
                                updatedReplies[replyIndex] = reply.copy(
                                    likeCount = response.likeCount,
                                    dislikeCount = response.dislikeCount,
                                    isLiked = response.commentLikeStatus == 1,
                                    isDisliked = response.commentLikeStatus == -1
                                )
                                updatedComments[index] = comment.copy(repliesList = updatedReplies)
                                updatedPosition = index
                                break
                            }
                        }

                        if (updatedPosition != null) {
                            break
                        }
                    }

                    //업데이트된 댓글 리스트를 _communityPostDetail에 반영
                    _communityPostDetail.value = postDetail.copy(postCommentList = updatedComments)

                    //변경 위치 반환
                    updatedPosition?.let { it }
                }

            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "Error reacting to comment: $e")
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                deletePostUseCase(postId)
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From deletePost, Error msg is $e")
            }
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            try {
                deleteCommentUseCase(commentId)
                //TODO 반환된 요소만 제거 후 다시 로드 필요
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From deleteComment, Error msg is $e")
            }
        }
    }
}
