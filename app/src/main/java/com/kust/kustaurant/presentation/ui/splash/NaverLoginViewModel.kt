package com.kust.kustaurant.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.usecase.auth.PostNaverLoginDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaverLoginViewModel @Inject constructor(
    private val postNaverLoginDataUseCase: PostNaverLoginDataUseCase
): ViewModel() {
    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> get() = _accessToken

    fun postNaverLogin(provider:String, providerId: String, naverAccessToken: String){
        viewModelScope.launch {
            try {
                val response = postNaverLoginDataUseCase.invoke(provider, providerId, naverAccessToken)
                _accessToken.value = response.accessToken
            }catch (e: Exception){
                // 에러 처리
                Log.e("NaverLoginViewModel", "Error posting Naver login", e)
            }
        }
    }
}