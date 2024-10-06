package com.kust.kustaurant.presentation.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityPostDetailViewModel @Inject constructor(
    private val getCommunityPostDetailUseCase: GetCommunityPostDetailUseCase,
) : ViewModel() {
    private val _communityPostDetail = MutableLiveData<CommunityPost>()
    val communityPostDetail: LiveData<CommunityPost> = _communityPostDetail

    private val _postLike = MutableLiveData<Boolean>(true)
    val postLike: LiveData<Boolean> = _postLike

    private val _scrap = MutableLiveData<Boolean>(true)
    val scrap: LiveData<Boolean> = _scrap

    fun loadCommunityPostDetail(postId: Int) {
        viewModelScope.launch {
            try {
                _communityPostDetail.value = getCommunityPostDetailUseCase(postId)
                Log.e("CommunityPostDetailViewModel", _communityPostDetail.value.toString())

            } catch (e: Exception) {
            }
        }
    }
}
