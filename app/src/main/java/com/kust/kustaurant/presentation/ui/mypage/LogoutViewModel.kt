package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.usecase.login.PostLogoutDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val postLogoutDataUseCase: PostLogoutDataUseCase
): ViewModel(){
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun postLogout(){
        viewModelScope.launch {
            try {
                val responseString = postLogoutDataUseCase.invoke()
                
                // 빈 값이면 성공, 값이 있으면 실패
                if (responseString.isBlank()) {
                    _response.value = "success"
                } else {
                    _response.value = "fail"
                    Log.e("LogoutViewModel", "로그아웃 실패: $responseString")
                }
            } catch (e: Exception) {
                _response.value = "fail"
                Log.e("LogoutViewModel", "오류 발생: ${e.message}")
            }
        }
    }
}