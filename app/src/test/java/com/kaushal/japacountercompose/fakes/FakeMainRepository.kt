package com.kaushal.japacountercompose.fakes

import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.MainRepository
import com.kaushal.japacountercompose.domain.UpdateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

data class UpdateCountCall(val id: Int, val newCount: Int, val updatedValue: Int, val updatedType: UpdateType)

/** In-memory [MainRepository] fake used to isolate ViewModels from real data access in tests. */
class FakeMainRepository : MainRepository {

    val japaListFlow = MutableStateFlow<List<JapaInfoEntities>>(emptyList())
    var getJapaListError: Throwable? = null

    val japaByIdFlows = mutableMapOf<Int, MutableStateFlow<JapaInfoEntities?>>()
    var getJapaByIdError: Throwable? = null

    val addJapaCalls = mutableListOf<Pair<String, Int?>>()
    var addJapaResult: Long = 1L
    var addJapaError: Throwable? = null

    val updateCountCalls = mutableListOf<UpdateCountCall>()
    var updateCountError: Throwable? = null

    val updateTargetCalls = mutableListOf<Pair<Int, Int>>()

    val markCompleteCalls = mutableListOf<Int>()
    var markCompleteError: Throwable? = null

    val resetCounterCalls = mutableListOf<Int>()
    var resetCounterError: Throwable? = null

    val deleteJapaCalls = mutableListOf<Int>()
    var deleteJapaError: Throwable? = null

    override fun getJapaList() = flow {
        getJapaListError?.let { throw it }
        emitAll(japaListFlow)
    }

    override fun getJapaById(id: Int) = flow {
        getJapaByIdError?.let { throw it }
        emitAll(japaByIdFlows.getOrPut(id) { MutableStateFlow(null) })
    }

    override suspend fun addJapa(name: String, target: Int?): Long {
        addJapaError?.let { throw it }
        addJapaCalls += name to target
        return addJapaResult
    }

    override suspend fun updateCount(id: Int, newCount: Int, updatedValue: Int, updatedType: UpdateType) {
        updateCountError?.let { throw it }
        updateCountCalls += UpdateCountCall(id, newCount, updatedValue, updatedType)
    }

    override suspend fun updateTarget(id: Int, target: Int) {
        updateTargetCalls += id to target
    }

    override suspend fun markComplete(id: Int) {
        markCompleteError?.let { throw it }
        markCompleteCalls += id
    }

    override suspend fun resetCounter(id: Int) {
        resetCounterError?.let { throw it }
        resetCounterCalls += id
    }

    override suspend fun deleteJapa(id: Int) {
        deleteJapaError?.let { throw it }
        deleteJapaCalls += id
    }
}
