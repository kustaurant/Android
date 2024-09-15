package com.kust.kustaurant.presentation.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityRanking
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostDetailUseCase
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostListUseCase
import com.kust.kustaurant.domain.usecase.community.GetCommunityRankingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getCommunityPostListUseCase: GetCommunityPostListUseCase,
    private val getCommunityRankingListUseCase: GetCommunityRankingListUseCase,
    private val getCommunityPostDetailUseCase: GetCommunityPostDetailUseCase,

    ) : ViewModel() {
    private val _postCategory = MutableLiveData<String>("all")
    val postCategory: LiveData<String> = _postCategory

    private val _page = MutableLiveData<Int>(0)
    val page: LiveData<Int> = _page

    private val _sort = MutableLiveData<String>("recent")
    val sort: LiveData<String> = _sort

    private val _communityPosts = MutableLiveData<List<CommunityPost>>()
    val communityPosts: LiveData<List<CommunityPost>> = _communityPosts

    private val _communityRanking = MutableLiveData<List<CommunityRanking>>()
    val communityRanking: LiveData<List<CommunityRanking>> = _communityRanking

    private val _communityPostDetail = MutableLiveData<CommunityPost>()
    val communityPostDetail: LiveData<CommunityPost> = _communityPostDetail

    private var isLastPage = false

    private val _rankingSortType = MutableLiveData<String>("")
    val rankingSortType: LiveData<String> = _rankingSortType

    init {
        loadCommunityPosts("all", 0, "recent")
    }

    fun loadCommunityPosts(postCategory: String, currentPage: Int, sort: String) {
        if (isLastPage) return

        viewModelScope.launch {
            try {
                val newPosts = getCommunityPostListUseCase(
                    postCategory,
                    currentPage,
                    sort
                )
                if (newPosts.isEmpty()) {
                    isLastPage = true
                } else {
                    val currentPosts = communityPosts.value.orEmpty().toMutableList()
                    currentPosts.addAll(newPosts)
                    _communityPosts.value = currentPosts
                    _page.value = page.value?.plus(1)
                }
            } catch (e: Exception) {
                Log.e("커뮤니티 뷰모델", "loadCommunityPosts Error", e)
            } finally {
                // 기타 처리
            }
        }
    }

    // 랭킹 데이터를 로드하고 상위 3명과 나머지를 분리
    fun loadCommunityRanking(sort: String) {
        viewModelScope.launch {
            try {
                val rankings = getCommunityRankingListUseCase(sort)
                _communityRanking.value = rankings
            } catch (e: Exception) {
                Log.e("커뮤니티 뷰모델", "loadCommunityRanking Error", e)
            }
        }
    }

    fun loadCommunityPostDetail(postId: Int) {
        viewModelScope.launch {
            try {
                _communityPostDetail.value = getCommunityPostDetailUseCase(postId)
            } catch (e: Exception) {
                Log.e("커뮤니티 뷰모델", "loadCommunityPostDetail Error", e)
            }
        }
    }



    fun resetPageAndLoad() {
        _page.value = 0
        isLastPage = false
        _communityPosts.value = emptyList()
        loadCommunityPosts(
            postCategory.value ?: "all",
            page.value ?: 0,
            sort.value ?: "recent"
        )
    }

    fun onSortTypeChanged(newSortType: String) {
        if (_rankingSortType.value != newSortType) {
            _rankingSortType.value = newSortType
            loadCommunityRanking(newSortType)
        }
    }

    fun onPostCategoryChanged(newCategory: String) {
        _postCategory.value = newCategory
        resetPageAndLoad()
    }

    fun onSortChanged(newSort: String) {
        _sort.value = newSort
        resetPageAndLoad()
    }

    fun onRankingDataChanged(rankings: List<CommunityRanking>): Pair<List<CommunityRanking>, List<CommunityRanking>> {
        return processRankingData(rankings)
    }

    private fun processRankingData(rankings: List<CommunityRanking>): Pair<List<CommunityRanking>, List<CommunityRanking>> {
        val topRankers = rankings.take(3)
        val remainingRankings = rankings.drop(3)
        return Pair(topRankers, remainingRankings)
    }
}
