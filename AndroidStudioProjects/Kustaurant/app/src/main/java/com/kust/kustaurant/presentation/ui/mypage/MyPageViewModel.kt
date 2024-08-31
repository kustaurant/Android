package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.data.model.MyProfileResponse
import com.kust.kustaurant.domain.usecase.mypage.GetMyCommunityCommentUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyCommunityListUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyEvaluateDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyFavoriteDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyPageDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyProfileDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.PatchMyProfileDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.PostMyFeedBackUseCase
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageDataUseCase: GetMyPageDataUseCase,
    private val getMyProfileDataUseCase: GetMyProfileDataUseCase,
    private val patchMyProfileDataUseCase: PatchMyProfileDataUseCase,
    private val getMyFavoriteDataUseCase: GetMyFavoriteDataUseCase,
    private val getMyCommunityCommentUseCase: GetMyCommunityCommentUseCase,
    private val getMyCommunityListUseCase: GetMyCommunityListUseCase,
    private val getMyEvaluateDataUseCase: GetMyEvaluateDataUseCase,
    private val postMyFeedBackUseCase: PostMyFeedBackUseCase
) : ViewModel() {
    private var originalProfileData: MyProfileResponse?= null

    private val _myPageData = MutableLiveData<MyPageResponse>()
    val myPageData: LiveData<MyPageResponse> = _myPageData

    private val _myProfileData = MutableLiveData<MyProfileResponse>()
    val myProfileData: LiveData<MyProfileResponse> = _myProfileData

    private val _myFavoriteData = MutableLiveData<List<MyFavoriteResponse>>()
    val myFavoriteData: LiveData<List<MyFavoriteResponse>> = _myFavoriteData

    private val _myCommentData = MutableLiveData<List<MyCommentResponse>>()
    val myCommentData: LiveData<List<MyCommentResponse>> = _myCommentData

    private val _myCommunityData = MutableLiveData<List<MyCommunityListResponse>>()
    val myCommunityData: LiveData<List<MyCommunityListResponse>> = _myCommunityData

    private val _myEvaluateData = MutableLiveData<List<MyEvaluateResponse>>()
    val myEvaluateData: LiveData<List<MyEvaluateResponse>> = _myEvaluateData

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    fun loadMyPageData(){
        viewModelScope.launch {
            val myPageData = getMyPageDataUseCase()
            _myPageData.postValue(myPageData)
            Log.d("mypage", myPageData.toString())
        }
    }


    fun loadMyProfileData() {
        viewModelScope.launch {
            val myProfileData = getMyProfileDataUseCase()
            originalProfileData = myProfileData
            _myProfileData.value = myProfileData ?: MyProfileResponse("", "", "")
        }
    }

    fun patchMyProfileData(nickname: String, email: String, phoneNumber: String) {
        viewModelScope.launch {
            try {
                patchMyProfileDataUseCase(nickname, email, phoneNumber)
                _toastMessage.postValue("프로필이 업데이트 되었습니다.")
                _updateSuccess.postValue(true)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                handleErrorResponse(errorBody)
            } catch (e: Exception) {
                _toastMessage.postValue("네트워크 에러가 발생했습니다.")
            }
        }
    }

    private fun handleErrorResponse(errorString: String?) {
        errorString?.let {
            val jsonObject = JSONObject(it)
            val errorMessage = jsonObject.getString("error")
            when {
                errorMessage.contains("전화번호는 숫자로 11자로만 입력되어야 합니다") -> postToast("전화번호는 숫자로 11자로만 입력되어야 합니다.")
                errorMessage.contains("닉네임을 변경한 지 30일이 지나지 않아 변경할 수 없습니다.") -> postToast("닉네임을 변경한 지 30일이 지나지 않아 변경할 수 없습니다.")
                errorMessage.contains("해당 닉네임이 이미 존재합니다.") -> postToast("해당 닉네임이 이미 존재합니다.")
                errorMessage.contains("이전과 동일한 닉네임입니다.") -> postToast("이전과 동일한 닉네임입니다.")
                errorMessage.contains("닉네임은 2자 이상이어야 합니다.") -> postToast("닉네임은 2자 이상이어야 합니다.")
                errorMessage.contains("닉네임은 10자 이하여야 합니다.") -> postToast("닉네임은 10자 이하여야 합니다.")

                else -> postToast("알 수 없는 에러가 발생했습니다: $errorMessage")
            }
        }
    }

    private fun postToast(message: String) {
        _toastMessage.postValue(message)
    }


    fun hasProfileChanged(nickname: String, email: String, phone: String): Boolean {
        return nickname != (originalProfileData?.nickname ?: "") ||
                email != (originalProfileData?.email ?: "") ||
                phone != (originalProfileData?.phoneNumber ?: "")
    }

    fun loadMyFavoriteData() {
        viewModelScope.launch {
            val myFavoriteData = getMyFavoriteDataUseCase()
            _myFavoriteData.postValue(myFavoriteData)
        }
    }

    fun loadMyCommentData(){
        viewModelScope.launch {
            val myCommentData = getMyCommunityCommentUseCase()
            _myCommentData.postValue(myCommentData)
        }
    }

    fun loadMyCommunityData(){
        viewModelScope.launch {
            val myCommunityData = getMyCommunityListUseCase()
            _myCommunityData.postValue(myCommunityData)
        }
    }

    fun loadMyEvaluateData(){
        viewModelScope.launch {
            val myEvaluateData = getMyEvaluateDataUseCase()
            _myEvaluateData.postValue(myEvaluateData)
        }
    }

    fun postFeedBackData(comment: String){
        viewModelScope.launch {
            try {
                postMyFeedBackUseCase(comment)
            } catch (e: Exception){
                Log.e("feedback error", e.toString())
            }
        }
    }

}

