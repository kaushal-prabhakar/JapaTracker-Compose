package com.kaushal.japacountercompose.data.repository

import com.kaushal.japacountercompose.data.JapaInfoDBEntity
import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.data.UpdateType
import com.kaushal.japacountercompose.data.db.RoomDBDao
import com.kaushal.japacountercompose.data.toJapaInfoEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val dao: RoomDBDao) : MainRepository {

    override fun getJapaList(): Flow<List<JapaInfoEntities>> {
        return dao.getMyJapas().map { list ->
            list.map { it.toJapaInfoEntities() }
        }
    }

    override fun getJapaById(id: Int): Flow<JapaInfoEntities?> {
        return dao.getJapaById(id).map { it?.toJapaInfoEntities() }
    }

    override suspend fun addJapa(name: String, target: Int?): Long {
        val entity = JapaInfoDBEntity(
            name = name,
            target = target,
            lastUpdatedTime = LocalDateTime.now(),
            currentCount = 0,
            updatedValue = 0,
            updatedType = UpdateType.INCREMENT,
            status = JapaStatus.NOT_STARTED
        )
        return dao.addNewJapa(entity)
    }

    override suspend fun updateCount(
        id: Int, newCount: Int, updatedValue: Int, updatedType: UpdateType
    ) {
        dao.updateCurrentCount(
            id = id,
            newCount = newCount,
            updatedValue = updatedValue,
            updatedType = updatedType,
            time = LocalDateTime.now(),
            status = JapaStatus.ACTIVE
        )
    }

    override suspend fun updateTarget(id: Int, target: Int) {
        dao.updateJapaTarget(id, target)
    }

    override suspend fun markComplete(id: Int) {
        dao.completeJapa(id, JapaStatus.COMPLETED, LocalDateTime.now())
    }

    override suspend fun resetCounter(id: Int) {
        dao.resetJapaCounter(id, LocalDateTime.now(), JapaStatus.NOT_STARTED)
    }

    override suspend fun deleteJapa(id: Int) {
        dao.deleteJapa(id)
    }
}
