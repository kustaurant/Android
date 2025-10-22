package com.kust.kustaurant.presentation.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.model.community.CommunityRanking
import com.kust.kustaurant.domain.model.community.toCategorySort
import com.kust.kustaurant.domain.model.community.toPostCategory
import com.kust.kustaurant.domain.model.community.toRankingSort
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostListUseCase
import com.kust.kustaurant.domain.usecase.community.GetCommunityRankingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getCommunityPostListUseCase: GetCommunityPostListUseCase,
    private val getCommunityRankingListUseCase: GetCommunityRankingListUseCase,
    ) : ViewModel() {
    private val _postCategory = MutableLiveData<String>("all")
    val postCategory: LiveData<String> = _postCategory

    private val _sort = MutableLiveData<String>("recent")
    val sort: LiveData<String> = _sort

    private val _communityPosts = MutableLiveData<List<CommunityPost>>()
    val communityPosts: LiveData<List<CommunityPost>> = _communityPosts

    private val _communityRanking = MutableLiveData<List<CommunityRanking>>()
    val communityRanking: LiveData<List<CommunityRanking>> = _communityRanking

    private var isLastPage = false
    private var currentPage = 0 //Start from 0

    private val _rankingSortType = MutableLiveData<String>("")

    private fun loadCommunityPosts(postCategory: String, currentPage: Int, sort: String) {
        if (isLastPage) return

        viewModelScope.launch {
            try {
                val newPosts = getCommunityPostListUseCase(
                    postCategory.toPostCategory(),
                    currentPage,
                    sort.toCategorySort()
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

    fun getCommunityPostList(state : CommunityPostListFragment.Companion.PostLoadState, sort : String) {
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

        loadCommunityPosts(_postCategory.value!!, currentPage, _sort.value!!)
    }

    fun updateSortAndLoadPosts(newSort: String) {
        if (_sort.value != newSort) {
            getCommunityPostList(CommunityPostListFragment.Companion.PostLoadState.POST_FIRST_PAGE, newSort)
        }
    }

    fun onPostCategoryChanged(newCategory: String) {
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

    fun onSortTypeChanged(newSortType: String) {
        if (_rankingSortType.value != newSortType) {
            _rankingSortType.value = newSortType
            loadCommunityRanking(newSortType)
        }
    }

    private fun loadCommunityRanking(sort: String) {
        viewModelScope.launch {
            try {
                val rankings = getCommunityRankingListUseCase(sort.toRankingSort())
                _communityRanking.value = rankings
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "From loadCommunityRanking Err is ", e)
            }
        }
    }
}
