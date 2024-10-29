package com.kust.kustaurant.data.model

data class EvaluationDataResponse(
    val evaluationScore: Double = 0.0,
    val evaluationSituations: List<Int>? = null,
    val evaluationImgUrl: String? = null,
    val evaluationComment: String? = null,
    val starComments: List<StarCommentsResponse> = emptyList(),
    val newImage: String? = null
)

data class StarCommentsResponse(
    val star : Double,
    val comment : String
)
