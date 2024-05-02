package com.example.doubletapphomework4.utils

import com.example.doubletapphomework4.ui.common.models.HabitData


fun List<HabitData>.indexOfById(habitData: HabitData): Int {
    this.forEachIndexed { index, habit ->
        if (habit.id == habitData.id) {
            return index
        }
    }

    return -1
}