package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.usecase.login.PostNewAccessTokenDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewAccessTokenViewModel @Inject constructor(
    private val postNewAccessTokenDataUseCase: PostNewAccessTokenDataUseCase
):ViewModel(){
    private val _accessToken = MutableLiveData<String?>()
    val accessToken: MutableLiveData<String?> get() = _accessToken

    fun postNewAccessToken(context: Context){
        viewModelScope.launch {
            try {
                val response = postNewAccessTokenDataUseCase.invoke()
                _accessToken.value = response.accessToken
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }
}