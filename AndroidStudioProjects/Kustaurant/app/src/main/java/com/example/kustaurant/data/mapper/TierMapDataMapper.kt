package com.example.kustaurant.data.mapper

import com.example.kustaurant.data.model.TierMapDataResponse
import com.example.kustaurant.presentation.ui.tier.TierMapData
import com.naver.maps.geometry.LatLng

fun TierMapDataResponse.toTierMapData(): TierMapData {
    return TierMapData(
        polygonCoords = this.polygonCoords.map { LatLng(it.latitude, it.longitude) },
        solidLines = this.solidLines.map { line -> line.map { LatLng(it.latitude, it.longitude) } },
        dashedLines = this.dashedLines.map { line -> line.map { LatLng(it.latitude, it.longitude) } }
    )
}