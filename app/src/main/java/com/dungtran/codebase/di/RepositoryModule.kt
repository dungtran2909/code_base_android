package com.dungtran.codebase.di

import com.dungtran.codebase.data.repository.firebase.AuthRepositoryImpl
import com.dungtran.codebase.data.repository.ProductRepositoryImpl
import com.dungtran.codebase.data.repository.firebase.ChatRepositoryImpl
import com.dungtran.codebase.data.repository.firebase.UserRepositoryImpl
import com.dungtran.codebase.domain.repository.firebase.AuthRepository
import com.dungtran.codebase.domain.repository.ProductRepository
import com.dungtran.codebase.domain.repository.firebase.ChatRepository
import com.dungtran.codebase.domain.repository.firebase.UserRepository
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
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
    
}

