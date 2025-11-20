package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.usecase.login.DeleteUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodByeViewModel @Inject constructor(
    private val deleteUserUseCase: DeleteUserUseCase
): ViewModel(){
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun deleteUser(){
        viewModelScope.launch {
            try {
                val responseString = deleteUserUseCase()
                
                // 빈 값이면 성공, 값이 있으면 실패
                if (responseString.isBlank()) {
                    _response.value = "success"
                } else {
                    _response.value = "fail"
                    Log.e("GoodByeViewModel", "회원탈퇴 실패: $responseString")
                }
            } catch (e: Exception) {
                _response.value = "fail"
                Log.e("GoodByeViewModel", "오류 발생: ${e.message}")
            }
        }
    }
}