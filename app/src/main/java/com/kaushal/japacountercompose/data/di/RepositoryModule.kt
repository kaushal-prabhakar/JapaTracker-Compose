package com.kaushal.japacountercompose.data.di

import com.kaushal.japacountercompose.data.repository.MainRepository
import com.kaushal.japacountercompose.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repositoryImpl: MainRepositoryImpl): MainRepository
}