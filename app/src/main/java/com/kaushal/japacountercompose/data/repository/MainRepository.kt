package com.kaushal.japacountercompose.data.repository

import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.UpdateType
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getJapaList(): Flow<List<JapaInfoEntities>>

    fun getJapaById(id: Int): Flow<JapaInfoEntities?>

    suspend fun addJapa(name: String, target: Int?): Long

    suspend fun updateCount(id: Int, newCount: Int, updatedValue: Int, updatedType: UpdateType)

    suspend fun updateTarget(id: Int, target: Int)

    suspend fun markComplete(id: Int)

    suspend fun resetCounter(id: Int)

    suspend fun deleteJapa(id: Int)
}
