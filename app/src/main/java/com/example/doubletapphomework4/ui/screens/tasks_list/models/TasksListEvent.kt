package com.example.doubletapphomework4.ui.screens.tasks_list.models

import com.example.doubletapphomework4.ui.common.models.HabitData

sealed class TasksListEvent {
    data class UploadHabit(val habitData: HabitData): TasksListEvent()
    data class IsSheetOpen(val showBottomSheen: Boolean): TasksListEvent()
    data class SearchCard(val searchText: String, val filter: Filters): TasksListEvent()
}