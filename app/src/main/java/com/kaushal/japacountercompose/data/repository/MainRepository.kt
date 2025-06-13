package com.kaushal.japacountercompose.data.repository

import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.UpdateType
import java.time.LocalDateTime

interface MainRepository {

    suspend fun addNewJapa(japaEntities: JapaInfoEntities)

    suspend fun getMyJapaList(): List<JapaInfoEntities>

    suspend fun getJapaDetails(name: String): JapaInfoEntities

    suspend fun updateCurrentCount(
        name: String, newCount: Int, updatedValue: Int,
        updatedType: UpdateType, time: LocalDateTime
    )

    suspend fun completeJapa(name: String)

    suspend fun resetCounter(name: String)

    suspend fun updateJapaTarget(name: String, target: Int)

    suspend fun deleteJapa(name: String)

}