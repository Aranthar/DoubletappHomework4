package com.example.doubletapphomework4.ui.screens.tasks_list.models

import com.example.doubletapphomework4.ui.common.models.HabitUI

sealed class TasksListEvent {
    data class UploadHabit(val habitData: HabitUI?): TasksListEvent()
    data class DeleteHabit(val habitData: HabitUI): TasksListEvent()
    data class IsSheetOpen(val showBottomSheen: Boolean): TasksListEvent()
    data class SearchCard(val searchText: String, val filter: Filters): TasksListEvent()
    data class DoneHabit(val index: Int, val habitData: HabitUI): TasksListEvent()
}