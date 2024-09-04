package com.kust.kustaurant.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.model.SearchRestaurant
import com.kust.kustaurant.domain.usecase.home.GetSearchDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchDataUseCase: GetSearchDataUseCase
):ViewModel(){
    private val _searchResults = MutableLiveData<List<SearchRestaurant>>()
    val searchResults: LiveData<List<SearchRestaurant>> = _searchResults

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun searchRestaurants(query: String) {
        viewModelScope.launch {
            try {
                val results = getSearchDataUseCase.invoke(query)
                _searchResults.value = results
                Log.d("SearchViewModel", "검색 결과: $results")
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}