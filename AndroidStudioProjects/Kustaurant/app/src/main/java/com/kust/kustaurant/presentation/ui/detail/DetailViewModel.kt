package com.kust.kustaurant.presentation.ui.detail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.domain.usecase.detail.GetCommentDataUseCase
import com.kust.kustaurant.domain.usecase.detail.GetDetailDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PostCommentDataUseCase
import com.kust.kustaurant.domain.usecase.detail.PostFavoriteToggleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailDataUseCase: GetDetailDataUseCase,
    private val getCommentDataUseCase: GetCommentDataUseCase,
    private val postCommentDataUseCase: PostCommentDataUseCase,
    private val postFavoriteToggleUseCase: PostFavoriteToggleUseCase
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
                // 댓글 작성 후, 댓글 목록을 새로 고침
                loadCommentData(restaurantId, "latest")
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


}