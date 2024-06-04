package com.example.doubletapphomework4.ui.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.common.repository.HabitRepository

@Database(entities = [HabitData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitRepository
}