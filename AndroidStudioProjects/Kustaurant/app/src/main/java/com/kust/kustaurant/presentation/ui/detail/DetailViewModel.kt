package com.kust.kustaurant.presentation.ui.detail

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataRequest
import com.kust.kustaurant.data.model.EvaluationDataResponse
import com.kust.kustaurant.domain.usecase.detail.DeleteCommentDataUseCase
import com.kust.kustaurant.domain.usecase.detail.GetCommentDataUseCase
import com.kust.kustaurant.domain.usecase.detail.GetDetailDataUseCase
import com.kust.kustaurant.domain.usecase.detail.GetEvaluationDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PostCommentDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PostCommentDisLikeUseCase
import com.kust.kustaurant.domain.usecase.detail.PostCommentLikeUseCase
import com.kust.kustaurant.domain.usecase.detail.PostEvaluationDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PostFavoriteToggleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.w3c.dom.Text
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailDataUseCase: GetDetailDataUseCase,
    private val getCommentDataUseCase: GetCommentDataUseCase,
    private val postCommentDataUseCase: PostCommentDataUseCase,
    private val postFavoriteToggleUseCase: PostFavoriteToggleUseCase,
    private val getEvaluationDataUseCase: GetEvaluationDataUseCase,
    private val postEvaluationDataUseCase: PostEvaluationDataUseCase,
    private val deleteCommentDataUseCase: DeleteCommentDataUseCase,
    private val postCommentLikeUseCase: PostCommentLikeUseCase,
    private val postCommentDisLikeUseCase: PostCommentDisLikeUseCase
): ViewModel() {
    val tabList = MutableLiveData(listOf("메뉴", "리뷰"))

    private val _detailData = MutableLiveData<DetailDataResponse>()
    val detailData: LiveData<DetailDataResponse> = _detailData

    private val _menuData = MutableLiveData<List<MenuData>>()
    val menuData: LiveData<List<MenuData>> = _menuData

    private val _tierData = MutableLiveData<TierInfoData>()
    val tierData: LiveData<TierInfoData> = _tierData

    private val _reviewData = MutableLiveData<List<CommentDataResponse>>()
    val reviewData: LiveData<List<CommentDataResponse>> = _reviewData

    private val _favoriteData = MutableLiveData<Boolean>()
    val favoriteData: LiveData<Boolean> = _favoriteData

    private val _evaluationData = MutableLiveData<EvaluationDataResponse>()
    val evaluationData: LiveData<EvaluationDataResponse> = _evaluationData

    private val _pEvaluationData = MutableLiveData<DetailDataResponse>()
    val pEvaluationData: LiveData<DetailDataResponse> = _pEvaluationData

    val evaluationComplete = MutableLiveData<Boolean>()

    fun loadDetailData(restaurantId : Int) {
        viewModelScope.launch {
            val getDetailData = getDetailDataUseCase(restaurantId)
            _detailData.value = getDetailData
            _menuData.value = getDetailData.restaurantMenuList.map{
                MenuData(it.menuId, it.menuName, it.menuPrice, it.naverType, it.menuImgUrl)
            }
            _tierData.value = TierInfoData(getDetailData.restaurantCuisineImgUrl, getDetailData.restaurantCuisine,
                getDetailData.mainTier, getDetailData.situationList)
        }
    }

    fun loadCommentData(restaurantId: Int, sort: String){
        viewModelScope.launch {
            val getCommentData = getCommentDataUseCase(restaurantId, sort)
            _reviewData.postValue(getCommentData)
        }
    }

    fun loadEvaluateData(restaurantId: Int){
        viewModelScope.launch {
            val getDetailData = getDetailDataUseCase(restaurantId)
            _detailData.value = getDetailData
        }
    }

    fun postCommentData(restaurantId: Int, commentId : Int, inputText: String){
        viewModelScope.launch {
            try {
                val response = postCommentDataUseCase(restaurantId, commentId, inputText)
            } catch (e: Exception) {
                Log.e("CommentPost", "Failed to post comment", e)
            }
        }
    }

    fun postFavoriteToggle(restaurantId: Int){
        viewModelScope.launch {
            try {
                val newFavoriteState = postFavoriteToggleUseCase(restaurantId)
                _detailData.value?.let {
                    val updatedDetailData = it.copy(isFavorite = newFavoriteState)
                    _detailData.postValue(updatedDetailData)
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Failed to toggle favorite", e)
            }
        }
    }

    fun loadMyEvaluationData(restaurantId: Int){
        viewModelScope.launch {
            val evaluationData = getEvaluationDataUseCase(restaurantId)
            _evaluationData.postValue(evaluationData)
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
                Log.d("taejung", keywords.toString())
                val imagePart: MultipartBody.Part? = imageUri?.let { uri ->
                    val file = getFileFromUri(context, uri)
                    val requestFile = file?.asRequestBody("image/jpg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("newImage", file?.name, requestFile!!)

                }
                postEvaluationDataUseCase(restaurantId, ratingPart, keywordParts, commentPart, imagePart)
                evaluationComplete.value = true
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Failed to post evaluation", e)
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

    fun deleteCommentData(restaurantId: Int, commentId: Int){
        viewModelScope.launch {
            try{
                deleteCommentDataUseCase(restaurantId, commentId)
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Failed to delete comment", e)
            }
        }
    }

    fun postCommentLike(restaurantId: Int, comment: Int){
        viewModelScope.launch {
            postCommentLikeUseCase(restaurantId, comment)
        }
    }

    fun postCommentDisLike(restaurantId: Int, comment: Int){
        viewModelScope.launch {
            postCommentDisLikeUseCase(restaurantId, comment)
        }
    }
}