package com.kust.kustaurant.presentation.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.domain.usecase.mypage.GetMyPageDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageDataUseCase: GetMyPageDataUseCase
) : ViewModel() {

    private val _myPageData = MutableLiveData<MyPageResponse>()
    val myPageData: LiveData<MyPageResponse> = _myPageData

    fun loadMyPageData(){
        viewModelScope.launch {
            val myPageData = getMyPageDataUseCase()
            _myPageData.postValue(myPageData)
        }
    }
}