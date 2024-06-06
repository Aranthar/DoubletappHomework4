package com.example.doubletapphomework4.ui.screens.tasks_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.doubletapphomework4.ui.common.enums.HabitPeriod
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.BaseViewModel
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.common.repository.HabitRepositoryImpl
import com.example.doubletapphomework4.ui.screens.tasks_list.models.Filters
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListEvent
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListViewState
import com.example.doubletapphomework4.utils.indexOfById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor(private val habitRepositoryImpl: HabitRepositoryImpl) :
    BaseViewModel<TasksListViewState, TasksListEvent>(initialState = TasksListViewState()) {
    private val habitsByType: Map<HabitType, MutableList<HabitUI>> = mapOf(
        HabitType.GOOD to mutableListOf(),
        HabitType.BAD to mutableListOf(),
    )
    private var showBottomSheet: Boolean = false
    private var searchText: String = ""
    private var currentFilter: Filters = Filters.NONE
    private var filteredList: MutableList<HabitUI> = mutableListOf()
    private var daysLeft: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun obtainEvent(viewEvent: TasksListEvent) {
        when (viewEvent) {
            is TasksListEvent.UploadHabit -> {
                viewModelScope.launch {
                    habitsByType[HabitType.GOOD]?.clear()
                    habitsByType[HabitType.BAD]?.clear()

                    habitRepositoryImpl.getAllHabits().forEach { habit ->
                        if (habit.type == HabitType.GOOD) {
                            habitsByType[HabitType.GOOD]?.add(habit)
                        } else {
                            habitsByType[HabitType.BAD]?.add(habit)
                        }
                    }

                    restoreHabits(viewEvent.habitData)

                    viewState.update { it.copy(habitsByType = habitsByType.toMap()) }
                }
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

            is TasksListEvent.DeleteHabit -> {
                habitsByType[viewEvent.habitData.type]?.remove(viewEvent.habitData)

                viewState.update { it.copy(habitsByType = habitsByType.toMap()) }

                viewModelScope.launch {
                    habitRepositoryImpl.deleteHabit(viewEvent.habitData)
                }
            }

            is TasksListEvent.DoneHabit -> {
                viewModelScope.launch {
                    val newHabit = habitDone(viewEvent.habitData)

                    habitsByType[viewEvent.habitData.type]?.set(
                        viewEvent.index,
                        newHabit
                    )
                    habitRepositoryImpl.updateHabit(newHabit)

                    viewState.update { it.copy(habitsByType = habitsByType.toMap()) }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun habitDone(habit: HabitUI): HabitUI {
        val endOfWeek = when (habit.period) {
            HabitPeriod.WEEK -> {
                LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            }

            HabitPeriod.MONTH -> {
                LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
            }

            HabitPeriod.YEAR -> {
                LocalDate.now().with(TemporalAdjusters.lastDayOfYear())
            }
        }
        val currentDaysLeft = ChronoUnit.DAYS.between(LocalDate.now(), endOfWeek)

        if (daysLeft < currentDaysLeft) {
            habit.currentRepeatCount = 0
            daysLeft = currentDaysLeft
        }
        habit.currentRepeatCount++

        return habit
    }

    private fun searchList(text: String, filter: Filters) {
        filteredList.clear()

        habitsByType[HabitType.GOOD]?.let { filteredList.addAll(it) }
        habitsByType[HabitType.BAD]?.let { filteredList.addAll(it) }

        filteredList = filteredList.sortedBy { it.title }.toMutableList() //Sorted

        searchByFilter(filter)

        if (text != "") {
            val filteredText = mutableListOf<HabitUI>()

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
                val filterMap = mutableMapOf<HabitPriority, MutableList<HabitUI>>(
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
                val filterMap = mutableMapOf<HabitType, MutableList<HabitUI>>(
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


    private fun restoreHabits(newHabit: HabitUI?) {
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
    }
}