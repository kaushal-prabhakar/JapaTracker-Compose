package com.kaushal.japacountercompose.data.di

import android.content.Context
import androidx.room.Room
import com.kaushal.japacountercompose.data.db.RoomDB
import com.kaushal.japacountercompose.data.db.RoomDBDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRoomDB(@ApplicationContext context: Context): RoomDB =
        Room.databaseBuilder(
            context.applicationContext,
            RoomDB::class.java,
            "JapaTracker"
        ).build()

    @Provides
    @Singleton
    fun providesDao(db: RoomDB): RoomDBDao = db.dao()
}
