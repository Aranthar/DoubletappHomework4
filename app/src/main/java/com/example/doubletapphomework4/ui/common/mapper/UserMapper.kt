package com.example.doubletapphomework4.ui.common.mapper

import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.common.models.HabitUI

fun HabitData.toUI(): HabitUI {
    return HabitUI(
        id = id,
        title = title,
        description = description,
        priority = priority,
        type = type,
        repeatCount = repeatCount,
        period = period,
    )
}

fun HabitUI.toModel(): HabitData {
    return HabitData(
        id = id,
        title = title,
        description = description,
        priority = priority,
        type = type,
        repeatCount = repeatCount,
        period = period,
    )
}