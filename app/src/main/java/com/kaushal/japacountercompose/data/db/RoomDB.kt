package com.kaushal.japacountercompose.data.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaushal.japacountercompose.data.JapaInfoDBEntity

@Database(entities = [JapaInfoDBEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class RoomDB: RoomDatabase() {

    abstract fun dao(): Dao
    companion object {
        @Volatile
        private var DB_INSTANCE: RoomDB? = null

        fun getRoomDBInstance(context: Context): RoomDB {
            return DB_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "JapaTracker"
                ).build()
                DB_INSTANCE = instance
                instance
            }
        }
    }
}