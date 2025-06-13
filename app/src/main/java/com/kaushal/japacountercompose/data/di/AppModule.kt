package com.kaushal.japacountercompose.data.di

import android.content.Context
import com.kaushal.japacountercompose.data.MoshiDateTimeAdapter
import com.kaushal.japacountercompose.data.db.RoomDB
import com.squareup.moshi.Moshi
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
    fun providesRoomDB(@ApplicationContext context: Context) = RoomDB.getRoomDBInstance(context)

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(MoshiDateTimeAdapter())
        .build()
}