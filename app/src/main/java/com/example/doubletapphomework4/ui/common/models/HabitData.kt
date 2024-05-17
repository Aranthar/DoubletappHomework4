package com.example.doubletapphomework4.ui.common.models

import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import java.util.UUID

data class HabitData(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val description: String = "",
    val priority: HabitPriority = HabitPriority.LOW,
    val type: HabitType = HabitType.GOOD,
    val repeatCount: String = "",
    val period: String = "",
)