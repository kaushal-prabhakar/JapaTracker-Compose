package com.kaushal.japacountercompose.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaushal.japacountercompose.data.JapaInfoDBEntity

@Database(entities = [JapaInfoDBEntity::class], version = 1, exportSchema = false)
abstract class RoomDB: RoomDatabase() {

    abstract fun dao(): Dao

}