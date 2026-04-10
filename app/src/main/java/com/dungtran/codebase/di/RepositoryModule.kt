package com.dungtran.codebase.di

import com.dungtran.codebase.data.repository.AuthRepositoryImpl
import com.dungtran.codebase.data.repository.ProductRepositoryImpl
import com.dungtran.codebase.domain.repository.AuthRepository
import com.dungtran.codebase.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl,
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}

