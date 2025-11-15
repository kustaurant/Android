package com.kust.kustaurant.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.domain.usecase.auth.PostNaverLoginDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaverLoginViewModel @Inject constructor(
    private val postNaverLoginDataUseCase: PostNaverLoginDataUseCase,
    private val prefs: AuthPreferenceDataSource,
): ViewModel() {
    val isLoginSuccessful = MutableLiveData<Boolean>(false)
    fun loginWithNaver(provider:String, providerId: String, naverAccessToken: String){
        viewModelScope.launch {
            try {
                val response = postNaverLoginDataUseCase(provider, providerId, naverAccessToken)
                prefs.setUserId(providerId)
                prefs.setAccessToken(response.accessToken)
                prefs.setRefreshToken(response.refreshToken)
                isLoginSuccessful.postValue(true)
            }catch (e: Exception){ 
                Log.e("NaverLoginViewModel", "Error posting Naver login", e) 
                isLoginSuccessful.postValue(false)
            }
        }
    }
}