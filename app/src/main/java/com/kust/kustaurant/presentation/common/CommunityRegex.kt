package com.kust.kustaurant.presentation.common

fun communityRegex(): Regex{
    val regex = "<img[^>]+src=\"([^\"]+)\"[^>]*>".toRegex()
    return regex
}