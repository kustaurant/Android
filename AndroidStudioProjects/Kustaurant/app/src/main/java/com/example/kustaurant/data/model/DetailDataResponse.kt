package com.example.kustaurant.data.model

data class DetailDataResponse (
    val restaurantId : Int,
    val restaurantImgUrl : String,
    val mainTier : Int,
    val restaurantCuisine : String,
    val restaurantCuisineImgUrl : String,
    val restaurantPosition : String,
    val restaurantName : String,
    val restaurantAddress : String,
    val isOpen : Boolean,
    val businessHours : String,
    val naverMapUrl : String,
    val situationList : List<String>,
    val partnershipInfo : String,
    val evaluationCount : Int,
    val restaurantScore : Double,
    val isEvaluated : Boolean,
    val isFavorite : Boolean,
    val favoriteCount : Int,
    val restaurantMenuList : List<DetailMenuResponse>
)

data class DetailMenuResponse(
    val menuId : Int,
    val menuName: String,
    val menuPrice : String,
    val naverType : String,
    val menuImgUrl : String
)