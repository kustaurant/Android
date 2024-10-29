package com.kust.kustaurant.data.model

data class EvaluationDataRequest(
    val evaluationScore: Double = 0.0,
    val evaluationSituations: List<Int> = emptyList(),
    val evaluationImgUrl: String? = null,
    val evaluationComment: String? = null,
    val starComments: List<StarCommentsRequest> = emptyList(),
    val newImage: String? = null
)

data class StarCommentsRequest(
    val star : Double,
    val comment : String
)
