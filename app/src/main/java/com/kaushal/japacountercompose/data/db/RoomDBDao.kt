package com.kaushal.japacountercompose.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaushal.japacountercompose.data.JapaInfoDBEntity
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.data.UpdateType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface RoomDBDao {

    @Query("SELECT * FROM Japa_Info")
    fun getMyJapas(): Flow<List<JapaInfoDBEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewJapa(japaInfoDBEntity: JapaInfoDBEntity): Long

    @Query("SELECT * FROM Japa_Info WHERE rowId = :id")
    fun getJapaById(id: Int): Flow<JapaInfoDBEntity?>

    @Query(
        "UPDATE Japa_Info SET CurrentCount = :newCount, LastUpdatedValue = :updatedValue, " +
                "LastUpdatedType = :updatedType, LastUpdatedTime = :time, Status = :status WHERE rowId = :id"
    )
    suspend fun updateCurrentCount(
        id: Int, newCount: Int, updatedValue: Int, updatedType: UpdateType,
        time: LocalDateTime, status: JapaStatus
    ): Int

    @Query(
        "UPDATE Japa_Info SET Status = :status, LastUpdatedTime = :updatedTime WHERE rowId = :id"
    )
    suspend fun completeJapa(id: Int, status: JapaStatus, updatedTime: LocalDateTime)

    @Query("UPDATE Japa_Info SET Target = :target WHERE rowId = :id")
    suspend fun updateJapaTarget(id: Int, target: Int)

    @Query(
        "UPDATE Japa_Info SET CurrentCount = 0, LastUpdatedValue = 0, LastUpdatedTime = :time, Status = :status WHERE rowId = :id"
    )
    suspend fun resetJapaCounter(id: Int, time: LocalDateTime, status: JapaStatus)

    @Query("DELETE FROM Japa_Info WHERE rowId = :id")
    suspend fun deleteJapa(id: Int)
}
