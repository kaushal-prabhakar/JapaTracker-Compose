package com.kaushal.japacountercompose.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaushal.japacountercompose.data.JapaInfoDBEntity

@Database(entities = [JapaInfoDBEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun dao(): RoomDBDao
}
