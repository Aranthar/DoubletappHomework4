package com.example.doubletapphomework4.ui.common.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.common.models.HabitUI

@Dao
interface HabitRepository {
    @Insert
    suspend fun insertHabit(habitData: HabitData)

    @Update
    suspend fun updateHabit(habitData: HabitData)

    @Delete
    suspend fun deleteHabit(habitData: HabitData)

    @Query("SELECT * FROM Habit")
    suspend fun getAllHabits(): List<HabitUI>
}