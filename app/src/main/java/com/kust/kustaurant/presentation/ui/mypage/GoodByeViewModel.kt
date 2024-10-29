package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.usecase.login.PostGoodByeDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class GoodByeViewModel @Inject constructor(
    private val postGoodByeDataUseCase: PostGoodByeDataUseCase
):ViewModel(){
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun postGoodBye(){
        viewModelScope.launch {
            try {
                // 서버에서 응답받은 JSON 문자열
                val responseString = postGoodByeDataUseCase.invoke()

                // JSON 파싱
                val jsonResponse = JSONObject(responseString)
                val status = jsonResponse.getString("status")
                val message = jsonResponse.getString("message")

                if (status == "OK" && message == "회원탈퇴가 성공적으로 이루어졌습니다.") {
                    _response.value = "success" // 성공 시 success 값 전달
                } else {
                    _response.value = "fail" // 실패 시 fail 값 전달
                }
            } catch (e: Exception) {
                // 에러 발생 시 fail 값 전달
                _response.value = "fail"
                Log.e("GoodByeViewModel", "오류 발생: ${e.message}")
            }
        }
    }
}