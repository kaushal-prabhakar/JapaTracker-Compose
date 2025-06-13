package com.kaushal.japacountercompose.data.repository

import com.kaushal.japacountercompose.data.JapaInfoDBEntity
import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.data.UpdateType
import com.kaushal.japacountercompose.data.db.RoomDB
import com.kaushal.japacountercompose.data.toJapaInfoEntities
import java.time.LocalDateTime
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val roomDB: RoomDB) : MainRepository {

    private val dao = roomDB.dao()

    override suspend fun addNewJapa(japaEntities: JapaInfoEntities) {
        val roomDBEntities = JapaInfoDBEntity(
            name = japaEntities.name,
            target = japaEntities.target,
            updatedValue = japaEntities.lastUpdatedValue,
            lastUpdatedTime = LocalDateTime.now(),
            currentCount = japaEntities.currentCount,
            updatedType = japaEntities.lastUpdatedType,
            status = japaEntities.status
        )

        val id = dao.addNewJapa(roomDBEntities)
        println("inserted row id: $id")
    }

    override suspend fun getMyJapaList(): List<JapaInfoEntities> {
        val roomDBEntities = dao.getMyJapas()
        return roomDBEntities.map {
            it.toJapaInfoEntities()
        }
    }

    override suspend fun getJapaDetails(name: String): JapaInfoEntities {
        return dao.getJapaDetails(name).toJapaInfoEntities()
    }

    override suspend fun updateCurrentCount(
        name: String,
        newCount: Int,
        updatedValue: Int,
        updatedType: UpdateType,
        time: LocalDateTime
    ) {
        dao.updateCurrentCount(name, newCount, updatedValue, updatedType, time)
    }

    override suspend fun completeJapa(name: String) {
        dao.completeJapa(name, JapaStatus.COMPLETED, LocalDateTime.now())
    }

    override suspend fun resetCounter(name: String) {
        dao.resetJapaCounter(name, LocalDateTime.now())
    }

    override suspend fun updateJapaTarget(name: String, target: Int) {
        dao.updateJapaTarget(name, target)
    }

    override suspend fun deleteJapa(name: String) {
        dao.deleteJapa(name)
    }
}