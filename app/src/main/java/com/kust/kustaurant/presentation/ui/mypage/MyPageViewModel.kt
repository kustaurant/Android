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
import com.kust.kustaurant.data.model.MyScrapResponse
import com.kust.kustaurant.domain.usecase.mypage.GetMyCommunityCommentUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyCommunityListUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyEvaluateDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyFavoriteDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyPageDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyProfileDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyScrapUseCase
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
    private val getMyScrapUseCase: GetMyScrapUseCase,
    private val getMyEvaluateDataUseCase: GetMyEvaluateDataUseCase,
    private val postMyFeedBackUseCase: PostMyFeedBackUseCase,
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

    private val _myScrapData = MutableLiveData<List<MyScrapResponse>>()
    val myScrapData: LiveData<List<MyScrapResponse>> = _myScrapData

    private val _myEvaluateData = MutableLiveData<List<MyEvaluateResponse>>()
    val myEvaluateData: LiveData<List<MyEvaluateResponse>> = _myEvaluateData

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    fun loadMyPageData(){
        viewModelScope.launch {
            try {
                val myPageData = getMyPageDataUseCase()
                _myPageData.postValue(myPageData)
            } catch (e : Exception){
                Log.e("마이페이지 뷰모델", "loadMyPageData Error", e)
            }
        }
    }


    fun loadMyProfileData() {
        viewModelScope.launch {
            try {
                val myProfileData = getMyProfileDataUseCase()
                originalProfileData = myProfileData
                _myProfileData.value = myProfileData ?: MyProfileResponse("", "", "")
            } catch (e: Exception) {
                Log.e("마이페이지 뷰모델", "loadMyProfileData Error", e)
            }
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
            try {
                val jsonObject = JSONObject(it)
                // v2 API는 "message" 필드를 사용하고, v1은 "error" 필드를 사용할 수 있음
                val errorMessage = jsonObject.optString("message", jsonObject.optString("error", ""))
                
                if (errorMessage.isNotEmpty()) {
                    when {
                        errorMessage.contains("전화번호는 숫자로 11자로만 입력되어야 합니다") -> postToast("전화번호는 숫자로 11자로만 입력되어야 합니다.")
                        errorMessage.contains("닉네임을 변경한 지 30일이 지나지 않아 변경할 수 없습니다.") -> postToast("닉네임을 변경한 지 30일이 지나지 않아 변경할 수 없습니다.")
                        errorMessage.contains("해당 닉네임이 이미 존재합니다.") -> postToast("해당 닉네임이 이미 존재합니다.")
                        errorMessage.contains("이전과 동일한 닉네임입니다.") -> postToast("이전과 동일한 닉네임입니다.")
                        errorMessage.contains("닉네임은 2자 이상이어야 합니다.") -> postToast("닉네임은 2자 이상이어야 합니다.")
                        errorMessage.contains("닉네임은 10자 이하여야 합니다.") -> postToast("닉네임은 10자 이하여야 합니다.")
                        errorMessage.contains("이미 사용 중인 전화번호") -> postToast("이미 사용 중인 전화번호 입니다.")

                        else -> postToast(errorMessage)
                    }
                } else {
                    postToast("알 수 없는 에러가 발생했습니다.")
                }
            } catch (e: Exception) {
                Log.e("마이페이지 뷰모델", "handleErrorResponse 파싱 에러", e)
                postToast("에러 응답을 처리하는 중 오류가 발생했습니다.")
            }
        } ?: run {
            postToast("네트워크 에러가 발생했습니다.")
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
            try {
                val myFavoriteData = getMyFavoriteDataUseCase()
                _myFavoriteData.postValue(myFavoriteData)
            } catch (e : Exception){
                Log.e("마이페이지 뷰모델", "loadMyFavoriteData Error", e)
            }
        }
    }

    fun loadMyCommentData(){
        viewModelScope.launch {
            try {
                val myCommentData = getMyCommunityCommentUseCase()
                _myCommentData.postValue(myCommentData)
            } catch (e:Exception){
                Log.e("마이페이지 뷰모델", "loadMyCommentData Error", e)
            }
        }
    }

    fun loadMyCommunityData(){
        viewModelScope.launch {
            try {
                val myCommunityData = getMyCommunityListUseCase()
                _myCommunityData.postValue(myCommunityData)
            } catch (e : Exception){
                Log.e("마이페이지 뷰모델", "loadMyCommunityData Error", e)
            }
        }
    }

    fun loadMyScrapData(){
        viewModelScope.launch {
            try {
                val myScrapData = getMyScrapUseCase()
                _myScrapData.postValue(myScrapData)
            } catch (e : Exception){
                Log.e("마이페이지 뷰모델", "loadScrapData Error", e)
            }
        }
    }

    fun loadMyEvaluateData(){
        viewModelScope.launch {
            try {
                val myEvaluateData = getMyEvaluateDataUseCase()
                _myEvaluateData.postValue(myEvaluateData)
            } catch (e : Exception) {
                Log.e("마이페이지 뷰모델", "loadMyEvaluateData Error", e)
            }
        }
    }

    fun loadMyPostData(){
        viewModelScope.launch {
            try {
                val myCommunityListData = getMyCommunityListUseCase()
                _myCommunityData.postValue(myCommunityListData)
            } catch (e : Exception){
                Log.e("마이페이지 뷰모델", "loadMyPostData Error", e)
            }
        }
    }

    fun postFeedBackData(comment: String){
        viewModelScope.launch {
            try {
                postMyFeedBackUseCase(comment)
            } catch (e: Exception){
                Log.e("마이페이지 뷰모델", "postFeedBackData Error", e)
            }
        }
    }

}

