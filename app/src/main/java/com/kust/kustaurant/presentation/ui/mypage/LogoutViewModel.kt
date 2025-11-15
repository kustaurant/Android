package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.common.session.SessionController
import com.kust.kustaurant.domain.model.appEvent.LogoutReason
import com.kust.kustaurant.domain.usecase.auth.PostLogoutDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val postLogoutDataUseCase: PostLogoutDataUseCase,
    private val session : SessionController
): ViewModel(){
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun postLogout() {
        viewModelScope.launch {
            postLogoutDataUseCase()
                .onSuccess {
                    _response.value = "success"
                    session.logout(LogoutReason.Manual)
                }
                .onFailure { e ->
                    _response.value = "fail"
                    Log.e("LogoutViewModel", "로그아웃 실패", e)
                }
        }
    }
}