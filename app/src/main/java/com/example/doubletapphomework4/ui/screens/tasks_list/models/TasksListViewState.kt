package com.example.doubletapphomework4.ui.screens.tasks_list.models

import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitData

data class TasksListViewState(
    val habitsByType: Map<HabitType, List<HabitData>> = mapOf(
        HabitType.GOOD to mutableListOf(),
        HabitType.BAD to mutableListOf(),
    ),
    val toType: HabitType = HabitType.GOOD,
    val showBottomSheet: Boolean = false,
    val searchText: String = "",
    val selectedFilter: Filters = Filters.NONE,
    val filteredList: List<HabitData> = listOf(),
)