package com.example.doubletapphomework4.ui.screens.tasks_list.models

import com.example.doubletapphomework4.ui.common.models.HabitData

sealed class TasksListEvent {
    data class UploadHabit(val habitData: HabitData): TasksListEvent()
}