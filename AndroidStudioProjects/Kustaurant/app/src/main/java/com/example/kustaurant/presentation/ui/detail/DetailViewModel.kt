package com.example.kustaurant.presentation.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kustaurant.R

class DetailViewModel : ViewModel() {
    val tabList = MutableLiveData(listOf("메뉴", "리뷰"))
    val tierData = MutableLiveData<ArrayList<TierInfoData>>()

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        tierData.value = arrayListOf(
            TierInfoData(R.drawable.img_category_korea, "한식", 1),
            TierInfoData(0, "혼밥", 0)
        )
    }
}