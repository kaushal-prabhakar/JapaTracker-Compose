package com.kaushal.japacountercompose.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.UpdateType
import java.time.LocalDateTime

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
