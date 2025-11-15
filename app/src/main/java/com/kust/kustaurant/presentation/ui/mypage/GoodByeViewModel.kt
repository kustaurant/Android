package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.common.session.SessionController
import com.kust.kustaurant.domain.model.appEvent.LogoutReason
import com.kust.kustaurant.domain.usecase.auth.DeleteUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodByeViewModel @Inject constructor(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val session : SessionController
) : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun postGoodBye() {
        viewModelScope.launch {
            try {
                deleteUserUseCase()
                _response.value = "success"
                session.logout(LogoutReason.Manual)
            } catch (e: Exception) {
                val errorMsg = when (e) {
                    is retrofit2.HttpException -> when (e.code()) {
                        401 -> "인증 정보 없음"
                        404 -> "사용자 없음"
                        else -> "서버 오류 (${e.code()})"
                    }
                    else -> e.message ?: "알 수 없는 오류 발생"
                }

                _response.value = "fail"
                Log.e("GoodByeViewModel", "회원탈퇴 실패: $errorMsg")
            }
        }
    }
}
