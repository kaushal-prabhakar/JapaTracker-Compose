package com.kaushal.japacountercompose.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaushal.japacountercompose.data.JapaInfoDBEntity
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.data.UpdateType
import java.time.LocalDateTime

@Dao
interface RoomDBDao {

    @Query("SELECT * FROM Japa_Info")
    suspend fun getMyJapas(): List<JapaInfoDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewJapa(japaInfoDBEntity: JapaInfoDBEntity): Long

    @Query(
        "UPDATE Japa_Info SET CurrentCount = :newCount, LastUpdatedValue = :updatedValue, " +
                "LastUpdatedType = :updatedType, LastUpdatedTime = :time WHERE JapaName = :name"
    )
    suspend fun updateCurrentCount(
        name: String, newCount: Int, updatedValue: Int, updatedType: UpdateType,
        time: LocalDateTime
    ): Int

    @Query("SELECT CurrentCount FROM Japa_Info WHERE JapaName = :name")
    suspend fun getCurrentCount(name: String): Int

    @Query("SELECT * FROM Japa_Info WHERE JapaName = :name")
    suspend fun getJapaDetails(name: String): JapaInfoDBEntity

    @Query(
        "UPDATE Japa_Info SET status = :status, LastUpdatedTime = :updatedTime WHERE JapaName = :name"
    )
    suspend fun completeJapa(
        name: String, status: JapaStatus, updatedTime: LocalDateTime
    )

    @Query("UPDATE Japa_Info SET Target = :target WHERE JapaName = :name")
    suspend fun updateJapaTarget(name: String, target: Int)

    @Query(
        "UPDATE Japa_Info SET CurrentCount = 0, LastUpdatedValue = 0, LastUpdatedTime = :time WHERE JapaName = :name"
    )
    suspend fun resetJapaCounter(name: String, time: LocalDateTime)

    @Query("DELETE FROM Japa_Info WHERE JapaName = :name")
    suspend fun deleteJapa(name: String)
}