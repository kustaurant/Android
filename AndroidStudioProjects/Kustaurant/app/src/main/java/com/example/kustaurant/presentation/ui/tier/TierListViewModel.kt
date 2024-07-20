package com.example.kustaurant.presentation.ui.tier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kustaurant.TierModel

class TierListViewModel : ViewModel() {
    private val _isExpanded = MutableLiveData<Boolean>(false)
    val isExpanded: LiveData<Boolean> = _isExpanded

    private val _tierList = MutableLiveData<List<TierModel>>()
    val tierList: LiveData<List<TierModel>> = _tierList

    init {
        loadTierList()
    }

    fun toggleExpand() {
        _isExpanded.value = _isExpanded.value?.not()
    }

    private fun loadTierList() {
        // 여기서 실제 데이터를 로드하는 로직을 구현합니다.
        // 예제 데이터를 사용합니다.
        _tierList.value = listOf(
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문", "", -1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(134, "호야초밥참치 본점", "일식", "건입~중문",  "", 1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(145, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(156, "호야초밥참치 본234235235235점", "일식", "건입~중문",  "", 3, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -2, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(13, "호야초밥참치 본점", "일식", "건입~중문",  "", 1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", 2, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            )
        )
    }
}