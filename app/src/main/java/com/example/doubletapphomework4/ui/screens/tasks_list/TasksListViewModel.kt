package com.example.doubletapphomework4.ui.screens.tasks_list

import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.BaseViewModel
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.screens.tasks_list.models.Filters
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
    private var showBottomSheet: Boolean = false
    private var searchText: String = ""
    private var currentFilter: Filters = Filters.NONE
    private var filteredList: MutableList<HabitData> = mutableListOf()

    override fun obtainEvent(viewEvent: TasksListEvent) {
        when (viewEvent) {
            is TasksListEvent.UploadHabit -> {
                restoreHabits(viewEvent.habitData)
            }

            is TasksListEvent.IsSheetOpen -> {
                showBottomSheet = viewEvent.showBottomSheen

                filteredList.clear()
                habitsByType[HabitType.GOOD]?.let { filteredList.addAll(it) }
                habitsByType[HabitType.BAD]?.let { filteredList.addAll(it) }
                filteredList = filteredList.sortedBy { it.title }.toMutableList() //Sorted

                viewState.update {
                    it.copy(
                        showBottomSheet = showBottomSheet,
                        filteredList = filteredList
                    )
                }
            }

            is TasksListEvent.SearchCard -> {
                searchText = viewEvent.searchText
                currentFilter = viewEvent.filter

                viewState.update {
                    it.copy(
                        searchText = searchText,
                        selectedFilter = currentFilter
                    )
                }

                searchList(searchText, currentFilter)

                viewState.update {
                    it.copy(filteredList = filteredList)
                }
            }
        }
    }

    private fun searchList(text: String, filter: Filters) {
        filteredList.clear()

        habitsByType[HabitType.GOOD]?.let { filteredList.addAll(it) }
        habitsByType[HabitType.BAD]?.let { filteredList.addAll(it) }

        filteredList = filteredList.sortedBy { it.title }.toMutableList() //Sorted

        searchByFilter(filter)

        if (text != "") {
            val filteredText = mutableListOf<HabitData>()

            filteredList.forEach { habitData ->
                if (habitData.title.lowercase().contains(text.lowercase())) {
                    filteredText.add(habitData)
                }
            }
            filteredList.clear()
            filteredList.addAll(filteredText)
        }
    }

    private fun searchByFilter(filter: Filters) {
        when (filter) {
            Filters.NONE -> filteredList
            Filters.PRIORITY -> {
                val filterMap = mutableMapOf<HabitPriority, MutableList<HabitData>>(
                    HabitPriority.HIGH to mutableListOf(),
                    HabitPriority.MEDIUM to mutableListOf(),
                    HabitPriority.LOW to mutableListOf()
                )

                filteredList.forEach { habitData ->
                    when (habitData.priority) {
                        HabitPriority.LOW -> filterMap[HabitPriority.LOW]?.add(habitData)
                        HabitPriority.MEDIUM -> filterMap[HabitPriority.MEDIUM]?.add(habitData)
                        HabitPriority.HIGH -> filterMap[HabitPriority.HIGH]?.add(habitData)
                    }
                }
                filteredList.clear()

                filterMap[HabitPriority.HIGH]?.let { filteredList.addAll(it) }
                filterMap[HabitPriority.MEDIUM]?.let { filteredList.addAll(it) }
                filterMap[HabitPriority.LOW]?.let { filteredList.addAll(it) }
            }

            Filters.TYPE -> {
                val filterMap = mutableMapOf<HabitType, MutableList<HabitData>>(
                    HabitType.GOOD to mutableListOf(),
                    HabitType.BAD to mutableListOf()
                )

                filteredList.forEach { habitData ->
                    when (habitData.type) {
                        HabitType.GOOD -> filterMap[HabitType.GOOD]?.add(habitData)
                        HabitType.BAD -> filterMap[HabitType.BAD]?.add(habitData)
                    }
                }
                filteredList.clear()

                filterMap[HabitType.GOOD]?.let { filteredList.addAll(it) }
                filterMap[HabitType.BAD]?.let { filteredList.addAll(it) }
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