package com.kaushal.japacountercompose.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

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
    val lastUpdatedTime: LocalDateTime
)

@Entity(tableName = "Japa_Info")
data class JapaInfoDBEntity(
    @PrimaryKey(autoGenerate = true) val rowId: Int = 0,
    @ColumnInfo(name = "JapaName") val name: String,
    @ColumnInfo(name = "Target") val target: Int?,
    @ColumnInfo(name = "LastUpdatedTime") val lastUpdatedTime: LocalDateTime,
    @ColumnInfo(name = "CurrentCount") val currentCount: Int,
    @ColumnInfo(name = "LastUpdatedValue") val updatedValue: Int,
    @ColumnInfo(name = "LastUpdatedType") val updatedType: UpdateType,
    @ColumnInfo(name = "Status") val status: JapaStatus
)

fun JapaInfoDBEntity.toJapaInfoEntities(): JapaInfoEntities {
    return JapaInfoEntities(
        id = this.rowId,
        name = this.name,
        target = this.target,
        status = this.status,
        currentCount = this.currentCount,
        lastUpdatedValue = this.updatedValue,
        lastUpdatedType = this.updatedType,
        lastUpdatedTime = this.lastUpdatedTime
    )
}
