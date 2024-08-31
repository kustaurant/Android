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
    val Response: LiveData<String> get() = _response

    fun postLogout(){
        viewModelScope.launch {
            try {
                val response = postLogoutDataUseCase.invoke()
                _response.value = response
            }catch (e: Exception){
                // 에러 처리
            }
        }
    }
}