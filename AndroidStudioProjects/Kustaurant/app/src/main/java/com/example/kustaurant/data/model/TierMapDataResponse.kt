package com.example.kustaurant.data.model

data class TierMapDataResponse(
    val polygonCoords: List<LatLngResponse>,
    val solidLines: List<List<LatLngResponse>>,
    val dashedLines: List<List<LatLngResponse>>
)

data class LatLngResponse(
    val latitude: Double,
    val longitude: Double
)