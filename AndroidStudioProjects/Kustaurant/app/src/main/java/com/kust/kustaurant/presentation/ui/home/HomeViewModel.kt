package com.kust.kustaurant.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.HomeResponse
import com.kust.kustaurant.domain.usecase.home.GetHomeDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {

    private val _homeResponse = MutableLiveData<HomeResponse>()
    val homeResponse: LiveData<HomeResponse> get() = _homeResponse

    init {
        getHomeData()
    }

    private fun getHomeData() {
        viewModelScope.launch {
            try {
                val response = getHomeDataUseCase.invoke()
                _homeResponse.postValue(response)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }
}