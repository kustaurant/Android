package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.data.model.MyProfileResponse
import com.kust.kustaurant.domain.usecase.mypage.GetMyCommunityCommentUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyCommunityListUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyFavoriteDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyPageDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.GetMyProfileDataUseCase
import com.kust.kustaurant.domain.usecase.mypage.PatchMyProfileDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageDataUseCase: GetMyPageDataUseCase,
    private val getMyProfileDataUseCase: GetMyProfileDataUseCase,
    private val patchMyProfileDataUseCase: PatchMyProfileDataUseCase,
    private val getMyFavoriteDataUseCase: GetMyFavoriteDataUseCase,
    private val getMyCommunityCommentUseCase: GetMyCommunityCommentUseCase,
    private val getMyCommunityListUseCase: GetMyCommunityListUseCase
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
                Log.d("phone", phoneNumber.toString())
                val response = patchMyProfileDataUseCase(nickname, email, phoneNumber)
            } catch (e: Exception) {
                Log.e("CommentPost", "Error posting comment", e)
            }
        }
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


}

