package com.kust.kustaurant.presentation.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.domain.model.community.CategorySort
import com.kust.kustaurant.domain.model.community.CommunityPostListItem
import com.kust.kustaurant.domain.model.community.CommunityRanking
import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.model.community.RankingSort
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostListUseCase
import com.kust.kustaurant.domain.usecase.community.GetCommunityRankingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val prefs : AuthPreferenceDataSource,
    private val getCommunityPostListUseCase: GetCommunityPostListUseCase,
    private val getCommunityRankingListUseCase: GetCommunityRankingListUseCase,
    ) : ViewModel() {
    private val _postCategory = MutableLiveData<PostCategory>(PostCategory.ALL)
    val postCategory: LiveData<PostCategory> = _postCategory

    private val _sort = MutableLiveData<CategorySort>(CategorySort.LATEST)
    val sort: LiveData<CategorySort> = _sort

    private val _communityPosts = MutableLiveData<List<CommunityPostListItem>>()
    val communityPosts: LiveData<List<CommunityPostListItem>> = _communityPosts

    private val _communityRanking = MutableLiveData<List<CommunityRanking>>()
    val communityRanking: LiveData<List<CommunityRanking>> = _communityRanking

    private var isLastPage = false
    private var currentPage = 0 //Start from 0

    private val _rankingSortType = MutableLiveData<RankingSort>(RankingSort.SEASONAL)

    init {
        loadCommunityRanking(RankingSort.SEASONAL)
    }

    private fun loadCommunityPosts(postCategory: PostCategory, currentPage: Int, sort: CategorySort) {
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
                    val currentPosts = _communityPosts.value.orEmpty() + newPosts
                    _communityPosts.postValue(currentPosts)
                }

            } catch (e: Exception) {
                Log.e("CommunityViewModel", "From loadCommunityPosts Err is ", e)
            } finally {  }
        }
    }

    fun getCommunityPostList(state : CommunityPostListFragment.Companion.PostLoadState, sort : CategorySort) {
        when(state) {
            CommunityPostListFragment.Companion.PostLoadState.POST_FIRST_PAGE -> {
                isLastPage = false
                _sort.value = sort
                currentPage = 0
                _communityPosts.value = emptyList()
            }
            CommunityPostListFragment.Companion.PostLoadState.POST_NEXT_PAGE -> {
                currentPage++
            }
        }

        if(_postCategory.value == null || _sort.value == null) {
            Log.e("CommunityViewModel", "From getCommunityPostList Err: postCategory or sort is null")
            return
        }
        loadCommunityPosts(_postCategory.value!!, currentPage, _sort.value!!)
    }

    fun updateSortAndLoadPosts(newSort: CategorySort) {
        if (_sort.value != newSort) {
            getCommunityPostList(CommunityPostListFragment.Companion.PostLoadState.POST_FIRST_PAGE, newSort)
        }
    }

    fun hasLoginInfo(): Boolean {
        prefs.getAccessToken()?.let {
            return true
        }
        return false
    }

    fun onPostCategoryChanged(newCategory: PostCategory) {
        if(_postCategory.value != newCategory) {
            _postCategory.value = newCategory
            getCommunityPostList(
                CommunityPostListFragment.Companion.PostLoadState.POST_FIRST_PAGE,
                _sort.value!!
            )
        }
    }

    /*
     * From below,
     * Related to CommunityPostRankingFragment
     */
    fun onRankingDataChanged(rankings: List<CommunityRanking>): Pair<List<CommunityRanking>, List<CommunityRanking>> {
        return processRankingData(rankings)
    }

    private fun processRankingData(rankings: List<CommunityRanking>): Pair<List<CommunityRanking>, List<CommunityRanking>> {
        val topRankers = rankings.take(3)
        val remainingRankings = rankings.drop(3)
        return Pair(topRankers, remainingRankings)
    }

    fun onSortTypeChanged(newSort: RankingSort) {
        if (_rankingSortType.value != newSort) {
            _rankingSortType.value = newSort
            loadCommunityRanking(newSort)
        }
    }

    private fun loadCommunityRanking(sort: RankingSort) {
        viewModelScope.launch {
            try {
                _communityRanking.value = getCommunityRankingListUseCase(sort)
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "From loadCommunityRanking Err is ", e)
            }
        }
    }
}
