package com.kust.kustaurant.presentation.common

import com.kust.kustaurant.R


data class CategoryItem(val imageResId: Int, val text: String)

object CategoryData {
    val categoryList = listOf(
        CategoryItem(R.drawable.img_category_all, "전체"),
        CategoryItem(R.drawable.img_category_korea, "한식"),
        CategoryItem(R.drawable.img_category_japan, "일식"),
        CategoryItem(R.drawable.img_category_china, "중식"),
        CategoryItem(R.drawable.img_category_western, "양식"),
        CategoryItem(R.drawable.img_category_asian, "아시안"),
        CategoryItem(R.drawable.img_category_meat, "고기"),
        CategoryItem(R.drawable.img_category_seafood, "해산물"),
        CategoryItem(R.drawable.img_category_chicken, "치킨"),
        CategoryItem(R.drawable.img_category_hamburger_pizza, "햄버거/피자"),
        CategoryItem(R.drawable.img_category_tteokbokki, "분식"),
        CategoryItem(R.drawable.img_category_beer, "술집"),
        CategoryItem(R.drawable.img_category_cafe, "카페/디저트"),
        CategoryItem(R.drawable.img_category_bakery, "베이커리"),
        CategoryItem(R.drawable.img_category_salad, "샐러드"),
        CategoryItem(R.drawable.img_category_benefit, "제휴업체")
    )
}
