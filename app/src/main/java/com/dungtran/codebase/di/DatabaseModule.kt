package com.dungtran.codebase.di

import android.content.Context
import androidx.room.Room
import com.dungtran.codebase.data.local.AppDatabase
import com.dungtran.codebase.data.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
}

