package com.example.kustaurant.presentation.ui.tier

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kustaurant.domain.usecase.GetTierMapDataUseCase
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TierMapViewModel @Inject constructor(
    private val getTierMapDataUseCase: GetTierMapDataUseCase
) : ViewModel() {

    private val _mapData = MutableLiveData<TierMapData>()
    val mapData: LiveData<TierMapData> = _mapData

    fun loadMapData(cuisines: String, situations: String, locations: String) {
        viewModelScope.launch {
            val encodedCuisines = Uri.encode(cuisines)
            val encodedSituations = Uri.encode(situations)
            val encodedLocations = Uri.encode(locations)
            _mapData.value = getTierMapDataUseCase(encodedCuisines, encodedSituations, encodedLocations)
        }
    }
}


data class TierMapData(
    val polygonCoords: List<LatLng>,
    val solidLines: List<List<LatLng>>,
    val dashedLines: List<List<LatLng>>
)
