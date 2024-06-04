package com.example.doubletapphomework4.ui.screens.tasks_list.models

import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitUI

data class TasksListViewState(
    val habitsByType: Map<HabitType, List<HabitUI>> = mapOf(
        HabitType.GOOD to listOf(),
        HabitType.BAD to listOf(),
    ),
    val toType: HabitType = HabitType.GOOD,
    val showBottomSheet: Boolean = false,
    val searchText: String = "",
    val selectedFilter: Filters = Filters.NONE,
    val filteredList: List<HabitUI> = listOf(),
)