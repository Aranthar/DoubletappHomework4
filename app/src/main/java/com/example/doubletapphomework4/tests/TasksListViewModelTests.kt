package com.example.doubletapphomework4.tests

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.doubletapphomework4.ui.common.enums.HabitPeriod
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.mapper.toModel
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.common.repository.HabitRepository
import com.example.doubletapphomework4.ui.common.repository.HabitRepositoryImpl
import com.example.doubletapphomework4.ui.screens.tasks_list.TasksListViewModel
import com.example.doubletapphomework4.ui.screens.tasks_list.models.Filters
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListEvent
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TasksListViewModelTest {
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var habitRepository: HabitRepository

    private lateinit var habitRepositoryImpl: HabitRepositoryImpl
    private lateinit var viewModel: TasksListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        habitRepositoryImpl = HabitRepositoryImpl(habitRepository)
        viewModel = TasksListViewModel(habitRepositoryImpl)

        Dispatchers.setMain(dispatcher = testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Test
    fun testUploadHabit() {
        runTest {
            // Given
            val habits = listOf(
                HabitUI(
                    id = 1,
                    title = "Good Habit 1",
                    type = HabitType.GOOD,
                    priority = HabitPriority.MEDIUM,
                    period = HabitPeriod.WEEK
                ),
                HabitUI(
                    id = 2,
                    title = "Bad Habit 1",
                    type = HabitType.BAD,
                    priority = HabitPriority.LOW,
                    period = HabitPeriod.MONTH
                )
            )
            Mockito.`when`(habitRepositoryImpl.getAllHabits()).thenReturn(habits)

            // When
            viewModel.obtainEvent(TasksListEvent.UploadHabit(habits[0]))
            viewModel.obtainEvent(TasksListEvent.UploadHabit(habits[1]))

            // Then
            val viewState = viewModel.getViewState().value
            assertEquals(1, viewState.habitsByType[HabitType.GOOD]?.size)
            assertEquals(1, viewState.habitsByType[HabitType.BAD]?.size)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Test
    fun testIsSheetOpen() {
        // Given
        val showBottomSheet = false

        // When
        viewModel.obtainEvent(TasksListEvent.IsSheetOpen(showBottomSheet))

        // Then
        val viewState = viewModel.getViewState().value
        assertEquals(showBottomSheet, viewState.showBottomSheet)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Test
    fun testSearchCard() {
        // Given
        val searchText = "habit"
        val filter = Filters.NONE

        // When
        viewModel.obtainEvent(TasksListEvent.SearchCard(searchText, filter))

        // Then
        val viewState = viewModel.getViewState().value
        assertEquals(searchText, viewState.searchText)
        assertEquals(filter, viewState.selectedFilter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Test
    fun testDeleteHabit() {
        runTest {
            // Given
            val habit = HabitUI(
                id = 1,
                title = "Habit",
                type = HabitType.GOOD,
                priority = HabitPriority.MEDIUM,
                period = HabitPeriod.WEEK
            )
            Mockito.`when`(habitRepositoryImpl.getAllHabits()).thenReturn(listOf(habit))

            viewModel.obtainEvent(TasksListEvent.UploadHabit(habit))

            // When
            viewModel.obtainEvent(TasksListEvent.DeleteHabit(habit))

            // Then
            val viewState = viewModel.getViewState().value
            assertTrue(viewState.habitsByType[HabitType.GOOD]?.isEmpty() == true)
            Mockito.verify(habitRepository).deleteHabit(habit.toModel())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Test
    fun testDoneHabit() {
        runTest {
            // Given
            val habit = HabitUI(
                id = 1,
                title = "Habit",
                type = HabitType.GOOD,
                priority = HabitPriority.MEDIUM,
                period = HabitPeriod.WEEK,
                currentRepeatCount = 0
            )
            Mockito.`when`(habitRepositoryImpl.getAllHabits()).thenReturn(listOf(habit))

            viewModel.obtainEvent(TasksListEvent.UploadHabit(habit))

            // When
            viewModel.obtainEvent(TasksListEvent.DoneHabit(0, habit))

            // Then
            val viewState = viewModel.getViewState().value
            assertEquals(1, viewState.habitsByType[HabitType.GOOD]?.get(0)?.currentRepeatCount)
            Mockito.verify(habitRepository).updateHabit(habit.copy(currentRepeatCount = 1).toModel())
        }
    }
}