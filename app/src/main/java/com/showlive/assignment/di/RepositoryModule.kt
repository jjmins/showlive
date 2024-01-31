package com.showlive.assignment.di

import com.showlive.assignment.data.repository.MarvelRepository
import com.showlive.assignment.data.repository.MarvelRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesMarvelRepository(repository: MarvelRepositoryImpl): MarvelRepository {
        return repository
    }
}