package com.kust.kustaurant.presentation.ui.community.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.data.model.commnity.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.commnity.CommunityPostScrapResponse
import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.model.community.CommunityPostComment
import com.kust.kustaurant.domain.model.community.LikeEvent
import com.kust.kustaurant.domain.usecase.community.DeleteCommunityCommentUseCase
import com.kust.kustaurant.domain.usecase.community.DeleteCommunityPostUseCase
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostDetailUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostCommentReactUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostCommentReplyUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostDetailScrapUseCase
import com.kust.kustaurant.domain.usecase.community.PostPostLikeUseCase
import com.kust.kustaurant.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CommunityPostDetailViewModel @Inject constructor(
    private val getCommunityPostDetail: GetCommunityPostDetailUseCase,
    private val postPostScrap: PostCommunityPostDetailScrapUseCase,
    private val postPostLikeUseCase: PostPostLikeUseCase,
    private val postCreateCommentReplyUseCase: PostCommunityPostCommentReplyUseCase,
    private val postCommentReactUseCase: PostCommunityPostCommentReactUseCase,
    private val deletePostUseCase: DeleteCommunityPostUseCase,
    private val deleteCommentUseCase: DeleteCommunityCommentUseCase,
    private val prefs : AuthPreferenceDataSource
) : ViewModel() {
    private val _postDetail = MutableLiveData<CommunityPost>()
    val postDetail: LiveData<CommunityPost> = _postDetail

    private val _postComments = MutableLiveData< List<CommunityPostComment>>()
    val postComments: LiveData<List<CommunityPostComment>> = _postComments

    private val _postCommentsCnt = MutableLiveData<Int>()
    val postCommentsCnt: LiveData<Int> = _postCommentsCnt

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

    fun loadCommunityPostDetail(postId: Long) {
        viewModelScope.launch {
            try {
                _postDetail.value = getCommunityPostDetail(postId)
                _postComments.value = _postDetail.value!!.comments ?: emptyList()
                _postMine.value = _postDetail.value!!.isPostMine
            } catch (e: Exception) {
                Log.e(
                    "CommunityPostDetailViewModel",
                    "From loadCommunityPostDetail, Error msg is $e"
                )
            }
        }
    }

    fun postPostDetailScrap(postId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _postScrapInfo.value = postPostScrap(postId, !(_postDetail.value?.isScrapped ?: false))
                val info = _postScrapInfo.value ?: return@launch

                _postDetail.value?.let {
                    _postDetail.value = it.copy(
                        isScrapped = info.isScrapped,
                        scrapCount = info.postScrapCount.toLong()
                    )
                }
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

    fun postPostLike(postId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val current = LikeEvent.from(_postDetail.value?.myReaction)

                val next: LikeEvent? = when (current) {
                    LikeEvent.LIKE -> null
                    LikeEvent.DISLIKE -> LikeEvent.LIKE
                    null -> LikeEvent.LIKE
                }

                _postLikeInfo.value = postPostLikeUseCase(postId, next)
                _postLikeInfo.value?.let { info ->
                    _postDetail.value = _postDetail.value?.copy(
                        myReaction = info.reactionType,
                        likeOnlyCount = info.likeCount.toLong(),
                    )
                }

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

    fun postCreateCommentReply(content: String, postId: Long, parentCommentId: Long?) {
        viewModelScope.launch {
            try {
                val created: CommunityPostComment =
                    postCreateCommentReplyUseCase(content, postId, parentCommentId)

                val current = _postDetail.value ?: return@launch
                val currentComments = current.comments.orEmpty()

                val newComments =
                    if (created.parentCommentId == null) {
                        listOf(created) + currentComments
                    } else {
                        val (updated, inserted) =
                            insertReply(currentComments, created.parentCommentId, created)
                        if (inserted) updated else listOf(created) + currentComments
                    }

                _postComments.value = newComments
                _postDetail.value = current.copy(
                    comments = newComments,
                    commentCount = current.commentCount + 1
                )

            } catch (e: Exception) {
                Log.e("CommunityPostDetailViewModel", "From postCommentReply, Error msg is $e", e)
            }
        }
    }

    private fun insertReply(
        list: List<CommunityPostComment>,
        parentId: Long,
        child: CommunityPostComment
    ): Pair<List<CommunityPostComment>, Boolean> {
        var inserted = false
        val updated = list.map { c ->
            when {
                c.commentId == parentId -> {
                    inserted = true
                    c.copy(replies = listOf(child) + c.replies)
                }
                else -> {
                    val (newReplies, did) = insertReply(c.replies, parentId, child)
                    if (did) {
                        inserted = true
                        c.copy(replies = newReplies)
                    } else c
                }
            }
        }
        return updated to inserted
    }

    fun postCommentReact(commentId: Long, action: String) {
        viewModelScope.launch {
            try {
                val current = _postComments.value ?: return@launch

                val currentReactionType: String? = current
                    .asSequence()
                    .flatMap { sequenceOf(it) + it.replies.asSequence() }
                    .firstOrNull { it.commentId == commentId }
                    ?.reactionType

                val likeEvent = when {
                    action == "LIKE" && currentReactionType == "LIKE" -> null
                    action == "DISLIKE" && currentReactionType == "DISLIKE" -> null
                    action == "LIKE" -> LikeEvent.LIKE
                    action == "DISLIKE" -> LikeEvent.DISLIKE
                    else -> null
                }

                val response = postCommentReactUseCase(commentId, likeEvent)

                _postComments.value?.let { cur ->
                    val updatedComments = cur.map { comment ->
                        if (comment.commentId == commentId) {
                            comment.copy(
                                likeCount = response.likeCount,
                                dislikeCount = response.dislikeCount,
                                reactionType = response.reactionType,
                                timeAgo = comment.timeAgo
                            )
                        } else {
                            val newReplies = comment.replies.map { reply ->
                                if (reply.commentId == commentId) {
                                    reply.copy(
                                        likeCount = response.likeCount,
                                        dislikeCount = response.dislikeCount,
                                        reactionType = response.reactionType,
                                        timeAgo = reply.timeAgo
                                    )
                                } else reply
                            }

                            comment.copy(replies = newReplies)
                        }
                    }

                    _postComments.postValue(updatedComments)
                }
            } catch (e: Exception) {
                Log.e(
                    "CommunityPostDetailViewModel",
                    "From postCommentReact, Error reacting to comment: $e"
                )
            }
        }
    }

    fun deletePost(postId: Long) {
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

    fun deleteComment(postId: Long, commentId: Long) {
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

    fun hasLoginInfo(): Boolean {
        prefs.getAccessToken()?.let {
            return true
        }
        return false
    }
}
