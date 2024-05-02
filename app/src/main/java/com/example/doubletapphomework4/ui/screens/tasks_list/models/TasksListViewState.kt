package com.example.doubletapphomework4.ui.screens.tasks_list.models

import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitData

data class TasksListViewState(
    val habitsByType: Map<HabitType, List<HabitData>> = mapOf(
        HabitType.GOOD to mutableListOf(),
        HabitType.BAD to mutableListOf(),
    ),
    val toType: HabitType = HabitType.GOOD,
)