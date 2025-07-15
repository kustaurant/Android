package com.kust.kustaurant.presentation.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.kust.kustaurant.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
) : ViewModel() {
    private val _communityPostDetail = MutableLiveData<CommunityPost>()
    val communityPostDetail: LiveData<CommunityPost> = _communityPostDetail

    private val _communityPostComments = MutableLiveData<List<CommunityPostComment>?>()
    val communityPostComments: LiveData<List<CommunityPostComment>?> = _communityPostComments

    private val _communityPostCommentsCnt = MutableLiveData<Int>()
    val communityPostCommentsCnt: LiveData<Int> = _communityPostCommentsCnt

    private val _postMine = MutableLiveData(true)
    val postMine: LiveData<Boolean> = _postMine

    private val _postDelete = MutableLiveData(false)
    val postDelete: LiveData<Boolean> = _postDelete

    private val _postScrapInfo = MutableLiveData<CommunityPostScrapResponse>()
    val postScrapInfo: LiveData<CommunityPostScrapResponse> = _postScrapInfo

    private val _postLikeInfo = MutableLiveData<CommunityPostLikeResponse>()
    val postLikeInfo: LiveData<CommunityPostLikeResponse> = _postLikeInfo

    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> = _uiState

    fun loadCommunityPostDetail(postId: Int) {
        viewModelScope.launch {
            try {
                _communityPostDetail.value = getCommunityPostDetail(postId)
                _postMine.value = _communityPostDetail.value!!.isPostMine
                _communityPostComments.value = _communityPostDetail.value!!.postCommentList
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
            _uiState.value = UiState.Loading
            try {
                _postScrapInfo.value = postPostScrap(postId)
                _uiState.value = UiState.Success(Unit)
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    _uiState.value = UiState.Error.Forbidden
                } else {
                    _uiState.value = UiState.Error.Other(e.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error.Other(e.message ?: "Unknown error")
                Log.e("CommunityPostDetailViewModel", "From postPostDetailScrap, Error msg is $e")
            }
        }
    }

    fun postPostLike(postId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _postLikeInfo.value = postPostLikeUseCase(postId)
                _uiState.value = UiState.Success(Unit)
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    _uiState.value = UiState.Error.Forbidden
                } else {
                    _uiState.value = UiState.Error.Other(e.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error.Other(e.message ?: "Unknown error")
                Log.e("CommunityPostDetailViewModel", "From postPostLike, Error msg is $e")
            }
        }
    }

    fun postCreateCommentReply(content: String, postId: Int, parentCommentId: Int? = null) {
        viewModelScope.launch {
            try {
                _communityPostComments.value = if (parentCommentId == null) {
                    postCreateCommentReplyUseCase(content, postId.toString(), "")
                } else {
                    postCreateCommentReplyUseCase(
                        content,
                        postId.toString(),
                        parentCommentId.toString()
                    )
                }

                var cnt = 0
                for (comment in _communityPostComments.value!!) {
                    cnt += 1 + comment.repliesList.size
                }

                _communityPostCommentsCnt.value = cnt
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From postCommentReply, Error msg is $e")
            }
        }
    }

    fun postCommentReact(commentId: Int, action: String) {
        viewModelScope.launch {
            try {
                val response = postCommentReactUseCase(commentId, action)

                _communityPostDetail.value?.let { currentPostDetail ->
                    // 댓글 순회
                    val updatedCommentList = currentPostDetail.postCommentList?.map { comment ->
                        if (comment.commentId == commentId) {
                            // 댓글이 업데이트된 경우
                            comment.copy(
                                likeCount = response.likeCount,
                                dislikeCount = response.dislikeCount,
                                isLiked = response.commentLikeStatus == 1,
                                isDisliked = response.commentLikeStatus == -1,
                                updatedAt = comment.updatedAt
                            )
                        } else {
                            // 대댓글 순회
                            val updatedReplies = comment.repliesList.map { reply ->
                                if (reply.commentId == commentId) {
                                    reply.copy(
                                        likeCount = response.likeCount,
                                        dislikeCount = response.dislikeCount,
                                        isLiked = response.commentLikeStatus == 1,
                                        isDisliked = response.commentLikeStatus == -1,
                                        updatedAt = reply.updatedAt
                                    )
                                } else {
                                    reply
                                }
                            }
                            comment.copy(repliesList = updatedReplies)
                        }
                    }

                    val updatedPostDetail =
                        currentPostDetail.copy(postCommentList = updatedCommentList)

                    // 변경된 객체를 LiveData에 설정하여 옵저버가 변경사항을 감지하도록 강제
                    _communityPostDetail.postValue(updatedPostDetail)
                }
            } catch (e: Exception) {
                Log.e(
                    "CommunityPostDetailViewModel",
                    "From postCommentReact, Error reacting to comment: $e"
                )
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                deletePostUseCase(postId)
                _postDelete.value = true
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From deletePost, Error msg is $e")
                _postDelete.value = false
            }
        }
    }

    fun deleteComment(postId: Int, commentId: Int) {
        viewModelScope.launch {
            try {
                deleteCommentUseCase(commentId)
                loadCommunityPostDetail(postId)
            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From deleteComment, Error msg is $e")
            }
        }
    }

    fun resetPostDelete() {
        _postDelete.value = false
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }
}
