package com.example.doubletapphomework4.ui.screens.tasks_list

import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.BaseViewModel
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListEvent
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListViewState
import com.example.doubletapphomework4.utils.indexOfById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor() :
    BaseViewModel<TasksListViewState, TasksListEvent>(initialState = TasksListViewState()) {
    private val habitsByType: Map<HabitType, MutableList<HabitData>> = mapOf(
        HabitType.GOOD to mutableListOf(),
        HabitType.BAD to mutableListOf(),
    )

    override fun obtainEvent(viewEvent: TasksListEvent) {
        when (viewEvent) {
            is TasksListEvent.UploadHabit -> {
                restoreHabits(viewEvent.habitData)
            }
        }
    }

    private fun restoreHabits(newHabit: HabitData?) {
        if (newHabit == null || !habitsByType.containsKey(newHabit.type)) return

        val goodList = habitsByType[HabitType.GOOD]!!
        val badList = habitsByType[HabitType.BAD]!!

        val goodListIndex = goodList.indexOfById(newHabit)
        val badListIndex = badList.indexOfById(newHabit)

        if (newHabit.type == HabitType.GOOD && goodListIndex != -1) {
            goodList[goodListIndex] = newHabit
        } else if (newHabit.type == HabitType.BAD && badListIndex != -1) {
            badList[badListIndex] = newHabit
        } else if (newHabit.type != HabitType.GOOD && goodListIndex != -1) {
            goodList.removeAt(goodListIndex)
            badList.add(newHabit)
        } else if (newHabit.type != HabitType.BAD && badListIndex != -1) {
            badList.removeAt(badListIndex)
            goodList.add(newHabit)
        } else {
            habitsByType[newHabit.type]?.add(newHabit)
        }

        viewState.update { it.copy(habitsByType = habitsByType) }
    }
}