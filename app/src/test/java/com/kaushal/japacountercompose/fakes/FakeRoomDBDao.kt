package com.kaushal.japacountercompose.fakes

import com.kaushal.japacountercompose.data.db.JapaInfoDBEntity
import com.kaushal.japacountercompose.data.db.RoomDBDao
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.UpdateType
import java.time.LocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** In-memory [RoomDBDao] used to verify [com.kaushal.japacountercompose.data.repository.MainRepositoryImpl] without Room. */
class FakeRoomDBDao : RoomDBDao {

    private val japas = MutableStateFlow<Map<Int, JapaInfoDBEntity>>(emptyMap())
    private var nextRowId = 1

    override fun getMyJapas() = japas.map { it.values.toList() }

    override suspend fun addNewJapa(japaInfoDBEntity: JapaInfoDBEntity): Long {
        val rowId = nextRowId++
        val entity = japaInfoDBEntity.copy(rowId = rowId)
        japas.value += (rowId to entity)
        return rowId.toLong()
    }

    override fun getJapaById(id: Int) = japas.map { it[id] }

    override suspend fun updateCurrentCount(
        id: Int, newCount: Int, updatedValue: Int, updatedType: UpdateType,
        time: LocalDateTime, status: JapaStatus
    ): Int {
        val existing = japas.value[id] ?: return 0
        japas.value += (id to existing.copy(
                    currentCount = newCount,
                    updatedValue = updatedValue,
                    updatedType = updatedType,
                    lastUpdatedTime = time,
                    status = status
                ))
        return 1
    }

    override suspend fun completeJapa(id: Int, status: JapaStatus, updatedTime: LocalDateTime) {
        val existing = japas.value[id] ?: return
        japas.value += (id to existing.copy(status = status, lastUpdatedTime = updatedTime))
    }

    override suspend fun updateJapaTarget(id: Int, target: Int) {
        val existing = japas.value[id] ?: return
        japas.value += (id to existing.copy(target = target))
    }

    override suspend fun resetJapaCounter(id: Int, time: LocalDateTime, status: JapaStatus) {
        val existing = japas.value[id] ?: return
        japas.value += (id to existing.copy(
                    currentCount = 0,
                    updatedValue = 0,
                    lastUpdatedTime = time,
                    status = status
                ))
    }

    override suspend fun deleteJapa(id: Int) {
        japas.value -= id
    }

    fun seed(entity: JapaInfoDBEntity): JapaInfoDBEntity {
        val rowId = if (entity.rowId != 0) entity.rowId else nextRowId++
        val seeded = entity.copy(rowId = rowId)
        japas.value += (rowId to seeded)
        nextRowId = maxOf(nextRowId, rowId + 1)
        return seeded
    }

    fun entityFor(id: Int): JapaInfoDBEntity? = japas.value[id]
}
