package com.kust.kustaurant.data.mapper

import android.util.Log
import com.kust.kustaurant.data.model.NonTieredRestaurantGroup
import com.kust.kustaurant.domain.model.TierRestaurant
import com.kust.kustaurant.data.model.TierMapData
import com.kust.kustaurant.data.model.TierMapDataResponse
import com.naver.maps.geometry.LatLng


fun TierMapDataResponse.toTierMapData(): TierMapData {
    val polygonCoords = this.solidPolygonCoordsList.flatten().map { LatLng(it.x, it.y) }
    val solidLines = this.solidPolygonCoordsList.map { line ->
        if (line.isNotEmpty() && line.first() != line.last()) {
            line.map { LatLng(it.x, it.y) } + LatLng(line.first().x, line.first().y)
        } else {
            line.map { LatLng(it.x, it.y) }
        }
    }
    val dashedLines = this.dashedPolygonCoordsList.map { line ->
        if (line.isNotEmpty() && line.first() != line.last()) {
            line.map { LatLng(it.x, it.y) } + LatLng(line.first().x, line.first().y)
        } else {
            line.map { LatLng(it.x, it.y) }
        }
    }

    val favoriteTierRestaurants = this.favoriteTierRestaurants?.map {
        TierRestaurant(
            restaurantId = it.restaurantId,
            restaurantRanking = it.restaurantRanking?.toIntOrNull() ?: 0,
            restaurantName = it.restaurantName ?: "Unknown",
            restaurantCuisine = it.restaurantCuisine ?: "Unknown",
            restaurantPosition = it.restaurantPosition ?: "Unknown",
            restaurantImgUrl = it.restaurantImgUrl ?: "",
            mainTier = it.mainTier,
            isEvaluated = it.isEvaluated,
            isFavorite = it.isFavorite,
            x = it.x.toDoubleOrNull() ?: 0.0,
            y = it.y.toDoubleOrNull() ?: 0.0,
            partnershipInfo = it.partnershipInfo ?: "",
            restaurantScore = it.restaurantScore?.toDoubleOrNull()?.takeIf { !it.isNaN() } ?: 0.0
        )
    } ?: emptyList()


    val tieredTierRestaurants = this.tieredRestaurants.map {
        TierRestaurant(
            restaurantId = it.restaurantId,
            restaurantRanking = it.restaurantRanking?.toIntOrNull() ?: 0,
            restaurantName = it.restaurantName,
            restaurantCuisine = it.restaurantCuisine,
            restaurantPosition = it.restaurantPosition,
            restaurantImgUrl = it.restaurantImgUrl,
            mainTier = it.mainTier,
            isEvaluated = it.isEvaluated,
            isFavorite = it.isFavorite,
            x = it.x.toDouble(),
            y = it.y.toDouble(),
            partnershipInfo = it.partnershipInfo ?: "",
            restaurantScore = it.restaurantScore?.toDoubleOrNull()?.takeIf { !it.isNaN() } ?: 0.0
        )
    }

    val nonTieredRestaurants = this.nonTieredRestaurants.map { group ->
        NonTieredRestaurantGroup(
            zoom = group.zoom,
            tierRestaurants = group.restaurants.map {
                TierRestaurant(
                    restaurantId = it.restaurantId,
                    restaurantRanking = it.restaurantRanking?.toIntOrNull() ?: 0,
                    restaurantName = it.restaurantName,
                    restaurantCuisine = it.restaurantCuisine,
                    restaurantPosition = it.restaurantPosition,
                    restaurantImgUrl = it.restaurantImgUrl,
                    mainTier = -1, // Non-tiered restaurants have mainTier as -1
                    isEvaluated = it.isEvaluated,
                    isFavorite = it.isFavorite,
                    x = it.x.toDouble(),
                    y = it.y.toDouble(),
                    partnershipInfo = it.partnershipInfo ?: "",
                    restaurantScore = it.restaurantScore?.toDoubleOrNull()?.takeIf { !it.isNaN() } ?: 0.0
                )
            }
        )
    }

    Log.d("TierMapDataMapper", "Polygon Coords: $polygonCoords")
    Log.d("TierMapDataMapper", "Solid Lines: $solidLines")
    Log.d("TierMapDataMapper", "Dashed Lines: $dashedLines")
    Log.d("TierMapDataMapper", "Tiered Restaurants: $tieredTierRestaurants")
    Log.d("TierMapDataMapper", "Non-Tiered Restaurants: $nonTieredRestaurants")

    return TierMapData(
        polygonCoords = polygonCoords,
        solidLines = solidLines,
        dashedLines = dashedLines,
        favoriteTierRestaurants = favoriteTierRestaurants,
        tieredTierRestaurants = tieredTierRestaurants,
        nonTieredRestaurants = nonTieredRestaurants,
        minZoom = this.minZoom,
        visibleBounds = this.visibleBounds
    )
}