package com.example.doubletapphomework4.ui.common.repository

import com.example.doubletapphomework4.ui.common.mapper.toModel
import com.example.doubletapphomework4.ui.common.models.HabitUI
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(private val repository: HabitRepository) {
    suspend fun getAllHabits(): List<HabitUI> {
        return repository.getAllHabits()
    }

    suspend fun insertHabit(habit: HabitUI) {
        repository.insertHabit(habit.toModel())
    }

    suspend fun updateHabit(habit: HabitUI) {
        repository.updateHabit(habit.toModel())
    }

    suspend fun deleteHabit(habit: HabitUI) {
        repository.deleteHabit(habit.toModel())
    }
}