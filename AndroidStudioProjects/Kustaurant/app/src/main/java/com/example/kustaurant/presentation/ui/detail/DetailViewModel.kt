package com.example.kustaurant.presentation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kustaurant.R
import com.example.kustaurant.data.model.DetailDataResponse
import com.example.kustaurant.domain.usecase.GetDetailDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailDataUseCase: GetDetailDataUseCase
): ViewModel() {
    val tabList = MutableLiveData(listOf("메뉴", "리뷰"))
    val tierData = MutableLiveData<ArrayList<TierInfoData>>()

    private val _detailData = MutableLiveData<DetailDataResponse>()
    val detailData: LiveData<DetailDataResponse> = _detailData

    private val _menuData = MutableLiveData<List<MenuData>>()
    val menuData: LiveData<List<MenuData>> = _menuData

    init {
        loadDummyData()
        loadDetailData(1)
    }

    private fun loadDetailData(restaurantId : Int) {
        viewModelScope.launch {
            val getDetailData = getDetailDataUseCase(restaurantId)
            _detailData.value = getDetailData
            _menuData.value = getDetailData.restaurantMenuList.map{
                MenuData(it.menuId, it.menuName, it.menuPrice, it.naverType, it.menuImgUrl)
            }
        }
    }

    private fun loadDummyData() {
        tierData.value = arrayListOf(
            TierInfoData(R.drawable.img_category_korea, "한식", 1),
            TierInfoData(0, "혼밥", 0)
        )
    }
}