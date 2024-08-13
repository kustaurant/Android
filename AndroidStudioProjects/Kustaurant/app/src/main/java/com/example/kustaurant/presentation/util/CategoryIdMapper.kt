package com.example.kustaurant.presentation.util
object CategoryIdMapper {
    val cuisineMap = mapOf(
        "전체" to "ALL",
        "한식" to "KO",
        "일식" to "JA",
        "중식" to "CH",
        "양식" to "WE",
        "아시안" to "AS",
        "고기" to "ME",
        "치킨" to "CK",
        "해산물" to "SE",
        "햄버거/피자" to "HP",
        "분식" to "BS",
        "술집" to "PU",
        "카페/디저트" to "CA",
        "베이커리" to "BA",
        "샐러드" to "SA",
        "제휴업체" to "JH"
    )

    val situationMap = mapOf(
        "전체" to "ALL",
        "혼밥" to "1",
        "2~4인" to "2",
        "5인 이상" to "3",
        "단체 회식" to "4",
        "배달" to "5",
        "야식" to "6",
        "친구 초대" to "7",
        "데이트" to "8",
        "소개팅" to "9"
    )

    val locationMap = mapOf(
        "전체" to "ALL",
        "건입~중문" to "L1",
        "중문~어대" to "L2",
        "후문" to "L3",
        "정문" to "L4",
        "구의역" to "L5"
    )

    fun mapMenus(menus: Set<String>): String {
        val mappedMenus = menus.map { menu ->
            cuisineMap[menu] ?: menu
        }
        return mappedMenus.joinToString(separator = ",")
    }

    fun mapSituations(situations: Set<String>): String {
        val mappedSituations = situations.map {situation ->
            situationMap[situation] ?: situation
        }
        return mappedSituations.joinToString(separator = ",")
    }

    fun mapLocations(locations: Set<String>): String {
        val mappedLocations = locations.map { location->
            locationMap[location] ?: location
        }
        return mappedLocations.joinToString(separator = ",")
    }
}