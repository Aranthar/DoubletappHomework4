package com.example.doubletapphomework4.ui.common.enums

enum class HabitType {
    GOOD, BAD;

    companion object {
        fun getFromOrdinal(ordinal: Int): HabitType = if (ordinal == 0) GOOD else BAD
    }
}