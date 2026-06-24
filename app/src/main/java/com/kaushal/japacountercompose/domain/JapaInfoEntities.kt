package com.kaushal.japacountercompose.domain

import androidx.compose.ui.graphics.Color

enum class JapaStatus {
    NOT_STARTED, ACTIVE, COMPLETED
}

enum class UpdateType {
    INCREMENT, DECREMENT
}

data class JapaInfoEntities(
    val id: Int = 0,
    val name: String,
    val target: Int?,
    val status: JapaStatus,
    val currentCount: Int,
    val lastUpdatedValue: Int,
    val lastUpdatedType: UpdateType,
    val lastUpdatedTime: String
)

data class JapaStatusUiInfo(
    val color: Color,
    val statusName: String
)
