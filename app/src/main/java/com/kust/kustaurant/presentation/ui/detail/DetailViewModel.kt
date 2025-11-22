package com.kust.kustaurant.presentation.ui.detail

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.ErrorResponse
import com.kust.kustaurant.data.model.EvalCommentReactionResponse
import com.kust.kustaurant.data.model.EvaluationDataRequest
import com.kust.kustaurant.data.model.EvaluationDataResponse
import com.kust.kustaurant.data.model.EvaluationReactionResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import com.kust.kustaurant.domain.usecase.detail.DeleteCommentDataUseCase
import com.kust.kustaurant.domain.usecase.detail.GetCommentDataUseCase
import com.kust.kustaurant.domain.usecase.detail.GetDetailDataUseCase
import com.kust.kustaurant.domain.usecase.detail.GetEvaluationDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PostCommentReplyDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PutEvalCommentReactionUseCase
import com.kust.kustaurant.domain.usecase.detail.PutEvaluationReactionUseCase
import com.kust.kustaurant.domain.usecase.detail.PostCommentReportUseCase
import com.kust.kustaurant.domain.usecase.detail.PostEvaluationDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PutFavoriteUseCase
import com.kust.kustaurant.domain.usecase.detail.DeleteFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getDetailDataUseCase: GetDetailDataUseCase,
    private val getCommentDataUseCase: GetCommentDataUseCase,
    private val postCommentReplyDataUseCase: PostCommentReplyDataUseCase,
    private val putFavoriteUseCase: PutFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val getEvaluationDataUseCase: GetEvaluationDataUseCase,
    private val postEvaluationDataUseCase: PostEvaluationDataUseCase,
    private val deleteCommentDataUseCase: DeleteCommentDataUseCase,
    private val postCommentReportUseCase : PostCommentReportUseCase,
    private val prefs: AuthPreferenceDataSource,
    private val putEvalCommentReactionUseCase: PutEvalCommentReactionUseCase,
    private val putEvaluationReactionUseCase: PutEvaluationReactionUseCase,
): ViewModel() {
    val tabList = MutableLiveData(listOf("메뉴", "리뷰"))

    private val _detailData = MutableLiveData<DetailDataResponse>()
    val detailData: LiveData<DetailDataResponse> = _detailData

    private val _menuData = MutableLiveData<List<MenuData>>()
    val menuData: LiveData<List<MenuData>> = _menuData

    private val _tierData = MutableLiveData<TierInfoData>()
    val tierData: LiveData<TierInfoData> = _tierData

    private var _reviewData = MutableLiveData<List<CommentDataResponse>>()
    val reviewData: LiveData<List<CommentDataResponse>> = _reviewData

    private val _favoriteData = MutableLiveData<Boolean>()
    val favoriteData: LiveData<Boolean> = _favoriteData

    private val _evaluationData = MutableLiveData<EvaluationDataResponse>()
    val evaluationData: LiveData<EvaluationDataResponse> = _evaluationData

    private val _pEvaluationData = MutableLiveData<DetailDataResponse>()
    val pEvaluationData: LiveData<DetailDataResponse> = _pEvaluationData

    private val _itemUpdateIndex = MutableLiveData<Int>()
    val itemUpdateIndex: LiveData<Int> = _itemUpdateIndex

    val evaluationComplete = MutableLiveData<Boolean>()
    private var currentSort = "POPULARITY"

    fun loadDetailData(restaurantId : Int) {
        viewModelScope.launch {
            try {
                val getDetailData = getDetailDataUseCase(restaurantId)
                _detailData.value = getDetailData
                _menuData.value = getDetailData.restaurantMenuList?.map {
                    MenuData(it.menuId, it.menuName, it.menuPrice, it.naverType, it.menuImgUrl)
                } ?: emptyList()
                _tierData.value = TierInfoData(
                    getDetailData.restaurantCuisineImgUrl, getDetailData.restaurantCuisine,
                    getDetailData.mainTier, getDetailData.situationList ?: emptyList()
                )
            } catch (e : Exception) {
                Log.e("디테일 뷰모델", "loadDetailData Error", e)
            }
        }
    }

    fun loadCommentData(restaurantId: Int, sort: String){
        currentSort = sort
        viewModelScope.launch {
            try{
                val getCommentData = getCommentDataUseCase(restaurantId, sort)
                _reviewData.postValue(getCommentData.toList())
            } catch (e: Exception){
                Log.e("디테일 뷰모델", "loadCommentData Error", e)
            }
        }
    }

    fun loadEvaluateData(restaurantId: Int){
        viewModelScope.launch {
            try {
                val getDetailData = getDetailDataUseCase(restaurantId)
                _detailData.value = getDetailData
            } catch (e: Exception){
                Log.e("디테일 뷰모델", "loadEvaluateData Error", e)
            }
        }
    }

    fun postCommentReplyData(restaurantId: Int, evalCommentId: Int, body: String) {
        viewModelScope.launch {
            try {
                postCommentReplyDataUseCase(restaurantId, evalCommentId, body)
                loadCommentData(restaurantId, currentSort)
            } catch (e: HttpException) {
                if (e.code() == 400 || e.code() == 404) {
                    val errorBody = e.response()?.errorBody()?.string()
                    handleErrorResponse(errorBody)
                } else {
                    Log.e("디테일 뷰모델", "postCommentReplyData Error", e)
                }
            } catch (e: Exception) {
                Log.e("디테일 뷰모델", "postCommentReplyData Error", e)
            }
        }
    }

    private fun handleErrorResponse(errorString: String?) {
        errorString?.let {
            try {
                val gson = Gson()
                val errorResponse = gson.fromJson(it, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                Log.e("디테일 뷰모델", "Error: $errorMessage")
                // 필요시 Toast 메시지나 LiveData로 에러 전달
            } catch (e: Exception) {
                Log.e("디테일 뷰모델", "handleErrorResponse 파싱 에러", e)
            }
        }
    }

    fun toggleFavorite(restaurantId: Int) {
        viewModelScope.launch {
            try {
                val currentFavoriteState = _detailData.value?.isFavorite ?: false
                val response = if (currentFavoriteState) {
                    // 현재 즐겨찾기 상태면 제거
                    deleteFavoriteUseCase(restaurantId)
                } else {
                    // 현재 즐겨찾기 상태가 아니면 추가
                    putFavoriteUseCase(restaurantId)
                }

                _detailData.value?.let { currentDetailData ->
                    val updatedDetailData = currentDetailData.copy(
                        isFavorite = response.isFavorite,
                        favoriteCount = response.count.toInt()
                    )
                    _detailData.postValue(updatedDetailData)
                }
            } catch (e: Exception) {
                Log.e("디테일 뷰모델", "toggleFavorite Error", e)
            }
        }
    }


    fun loadMyEvaluationData(restaurantId: Int){
        viewModelScope.launch {
            try {
                val evaluationData = getEvaluationDataUseCase(restaurantId)
                _evaluationData.postValue(evaluationData)
            } catch (e : Exception){
                Log.e("디테일 뷰모델", "loadMyEvaluationData Error", e)
            }
        }
    }

    fun postEvaluationData(context: Context, restaurantId: Int, rating: Double, comment: String, keywords: List<Int>, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val ratingPart = rating.toString().toRequestBody(MultipartBody.FORM)
                val commentPart = comment.toRequestBody(MultipartBody.FORM)
                val keywordParts = keywords.map { keyword ->
                    MultipartBody.Part.createFormData("evaluationSituations", keyword.toString())
                }
                val imagePart: MultipartBody.Part? = imageUri?.let { uri ->
                    val file = getFileFromUri(context, uri)
                    val requestFile = file?.asRequestBody("image/jpg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("newImage", file?.name, requestFile!!)
                }
                val commentData = postEvaluationDataUseCase(restaurantId, ratingPart, keywordParts, commentPart, imagePart)
                Log.d("commentData", comment.toString())
                evaluationComplete.value = true
                _reviewData.postValue(commentData.toList())
            } catch (e: Exception) {
                Log.e("디테일 뷰모델", "postEvaluationData Error", e)
            }
        }
    }


    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        return tempFile
    }

    fun deleteCommentData(restaurantId: Int, evalCommentId: Int) {
        viewModelScope.launch {
            try {
                deleteCommentDataUseCase(restaurantId, evalCommentId)
                loadCommentData(restaurantId, currentSort)
            } catch (e: Exception) {
                Log.e("디테일 뷰모델", "deleteCommentData Error", e)
            }
        }
    }


    fun postCommentReport(restaurantId: Int, commentId: Int){
        try {
            viewModelScope.launch {
                postCommentReportUseCase(restaurantId, commentId)
            }
        } catch (e: Exception){
            Log.d("디테일 뷰모델", "postCommentReport Error", e)
        }
    }

    fun putEvalCommentReaction(evalCommentId: Int, reaction: String?) {
        viewModelScope.launch {
            try {
                val response = putEvalCommentReactionUseCase(evalCommentId, reaction)
                updateReplyData(response, evalCommentId)
            } catch (e: Exception) {
                Log.e("디테일 뷰모델", "putEvalCommentReaction Error", e)
            }
        }
    }

    fun putEvaluationReaction(evaluationId: Int, reaction: String?) {
        viewModelScope.launch {
            try {
                val accessToken = prefs.getAccessToken()
                val role = if (accessToken != null) "USER" else "GUEST"
                val userId = 0
                val response = putEvaluationReactionUseCase(evaluationId, reaction, userId, role)
                updateEvaluationData(response, evaluationId)
            } catch (e: Exception) {
                Log.e("디테일 뷰모델", "putEvaluationReaction Error", e)
            }
        }
    }

    fun toggleEvalCommentLike(evaluationId: Int) {
        val currentReview = _reviewData.value?.find { it.evalId == evaluationId }
        val currentReaction = currentReview?.reactionType
        val newReaction = when (currentReaction) {
            "LIKE" -> null // 좋아요 해제
            else -> "LIKE" // 좋아요 설정 또는 변경
        }
        putEvaluationReaction(evaluationId, newReaction)
    }

    fun toggleEvalCommentDislike(evaluationId: Int) {
        val currentReview = _reviewData.value?.find { it.evalId == evaluationId }
        val currentReaction = currentReview?.reactionType
        val newReaction = when (currentReaction) {
            "DISLIKE" -> null // 싫어요 해제
            else -> "DISLIKE" // 싫어요 설정 또는 변경
        }
        putEvaluationReaction(evaluationId, newReaction)
    }

    fun toggleEvalCommentReplyLike(commentId: Int) {
        val currentReview = _reviewData.value?.find { review ->
            review.evalCommentList?.any { it.commentId == commentId } == true
        }
        val currentReply = currentReview?.evalCommentList?.find { it.commentId == commentId }
        val currentReaction = currentReply?.reactionType
        val newReaction = when (currentReaction) {
            "LIKE" -> null // 좋아요 해제
            else -> "LIKE" // 좋아요 설정 또는 변경
        }
        putEvalCommentReaction(commentId, newReaction)
    }

    fun toggleEvalCommentReplyDislike(commentId: Int) {
        val currentReview = _reviewData.value?.find { review ->
            review.evalCommentList?.any { it.commentId == commentId } == true
        }
        val currentReply = currentReview?.evalCommentList?.find { it.commentId == commentId }
        val currentReaction = currentReply?.reactionType
        val newReaction = when (currentReaction) {
            "DISLIKE" -> null // 싫어요 해제
            else -> "DISLIKE" // 싫어요 설정 또는 변경
        }
        putEvalCommentReaction(commentId, newReaction)
    }

    private fun updateEvaluationData(response: EvaluationReactionResponse, evaluationId: Int) {
        _reviewData.value?.let { currentReviews ->
            val updatedReviews = currentReviews.map { review ->
                if (review.evalId == evaluationId) {
                    review.copy(
                        reactionType = response.reaction,
                        evalLikeCount = response.likeCount,
                        evalDislikeCount = response.dislikeCount
                    )
                } else {
                    review
                }
            }
            _reviewData.postValue(updatedReviews)
        }
    }

    private fun updateReplyData(response: EvalCommentReactionResponse, evalCommentId: Int) {
        _reviewData.value?.let { currentReviews ->
            val updatedReviews = currentReviews.map { review ->
                val updatedReplies = review.evalCommentList?.map { reply ->
                    if (reply.commentId == evalCommentId) {
                        reply.copy(
                            reactionType = response.reaction,
                            commentLikeCount = response.likeCount,
                            commentDislikeCount = response.dislikeCount
                        )
                    } else {
                        reply
                    }
                }
                review.copy(evalCommentList = updatedReplies)
            }
            _reviewData.postValue(updatedReviews)
        }
    }


    fun hasLoginInfo(): Boolean {
        prefs.getAccessToken()?.let {
            return true
        }
        return false
    }
}