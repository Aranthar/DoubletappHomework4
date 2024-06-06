package com.example.doubletapphomework4.ui.common.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.doubletapphomework4.ui.common.enums.HabitPeriod
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import kotlin.random.Random

@Entity(tableName = "Habit")
data class HabitData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = Random.nextInt(),
    val title: String = "",
    val description: String = "",
    val priority: HabitPriority = HabitPriority.LOW,
    val type: HabitType = HabitType.GOOD,
    val repeatCount: String = "",
    val currentRepeatCount: Int = 0,
    val period: HabitPeriod = HabitPeriod.WEEK,
)