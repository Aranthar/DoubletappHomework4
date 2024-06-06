package com.example.doubletapphomework4.ui.common.models

import com.example.doubletapphomework4.ui.common.enums.HabitPeriod
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import kotlin.random.Random

data class HabitUI(
    val id: Int = Random.nextInt(),
    val title: String = "",
    val description: String = "",
    val priority: HabitPriority = HabitPriority.LOW,
    val type: HabitType = HabitType.GOOD,
    val repeatCount: String = "",
    var currentRepeatCount: Int = 0,
    val period: HabitPeriod = HabitPeriod.WEEK,
)