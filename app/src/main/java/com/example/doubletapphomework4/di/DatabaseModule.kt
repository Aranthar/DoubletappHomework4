package com.example.doubletapphomework4.di

import android.content.Context
import androidx.room.Room
import com.example.doubletapphomework4.ui.common.AppDatabase
import com.example.doubletapphomework4.ui.common.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun getHabitRepository(@ApplicationContext appContext: Context): HabitRepository {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "Habit")
            .build()
            .habitDao()
    }
}