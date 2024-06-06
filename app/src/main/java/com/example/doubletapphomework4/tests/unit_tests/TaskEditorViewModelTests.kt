package com.example.doubletapphomework4.tests.unit_tests

import com.example.doubletapphomework4.ui.common.enums.HabitFieldType
import com.example.doubletapphomework4.ui.common.enums.HabitPeriod
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.mapper.toModel
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.common.repository.HabitRepository
import com.example.doubletapphomework4.ui.common.repository.HabitRepositoryImpl
import com.example.doubletapphomework4.ui.screens.task_editor.TaskEditorViewModel
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorEvent
import junit.framework.TestCase.assertEquals
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
class TaskEditorViewModelTest {
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var habitRepository: HabitRepository

    private lateinit var habitRepositoryImpl: HabitRepositoryImpl
    private lateinit var viewModel: TaskEditorViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        habitRepositoryImpl = HabitRepositoryImpl(habitRepository)
        viewModel = TaskEditorViewModel(habitRepositoryImpl)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testChangeHabitType() {
        // Given
        val type = HabitType.GOOD

        // When
        viewModel.obtainEvent(TaskEditorEvent.ChangeHabitType(type))

        // Then
        val viewState = viewModel.getViewState().value
        assertEquals(type, viewState.habitData.type)
    }

    @Test
    fun testChangePriority() {
        // Given
        val priority = HabitPriority.HIGH

        // When
        viewModel.obtainEvent(TaskEditorEvent.ChangePriority(priority))

        // Then
        val viewState = viewModel.getViewState().value
        assertEquals(priority, viewState.habitData.priority)
    }

    @Test
    fun testChangePeriod() {
        // Given
        val period = HabitPeriod.WEEK

        // When
        viewModel.obtainEvent(TaskEditorEvent.ChangePeriod(period))

        // Then
        val viewState = viewModel.getViewState().value
        assertEquals(period, viewState.habitData.period)
    }

    @Test
    fun testChangeFieldText() {
        // Given
        val title = "New Habit Title"
        val description = "New Habit Description"
        val repeatCount = "5"

        // When
        viewModel.obtainEvent(TaskEditorEvent.ChangeFieldText(HabitFieldType.TITLE, title))
        viewModel.obtainEvent(
            TaskEditorEvent.ChangeFieldText(
                HabitFieldType.DESCRIPTION,
                description
            )
        )
        viewModel.obtainEvent(
            TaskEditorEvent.ChangeFieldText(
                HabitFieldType.REPEAT_COUNT,
                repeatCount
            )
        )

        // Then
        val viewState = viewModel.getViewState().value
        assertEquals(title, viewState.habitData.title)
        assertEquals(description, viewState.habitData.description)
        assertEquals(repeatCount, viewState.habitData.repeatCount)
    }

    @Test
    fun testClickBtnSave() = runTest {
        // Given
        val habitData = HabitUI(
            id = 1,
            title = "Habit",
            type = HabitType.GOOD,
            priority = HabitPriority.MEDIUM,
            period = HabitPeriod.WEEK
        )
        viewModel.obtainEvent(TaskEditorEvent.UploadHabit(habitData))

        // When
        viewModel.obtainEvent(TaskEditorEvent.ClickBtnSave)

        // Then
        Mockito.verify(habitRepository).updateHabit(habitData.toModel())
    }

    @Test
    fun testUploadHabit() {
        // Given
        val habitData = HabitUI(
            id = 1,
            title = "Habit",
            type = HabitType.GOOD,
            priority = HabitPriority.MEDIUM,
            period = HabitPeriod.WEEK
        )

        // When
        viewModel.obtainEvent(TaskEditorEvent.UploadHabit(habitData))

        // Then
        val viewState = viewModel.getViewState().value
        assertEquals(habitData, viewState.habitData)
    }
}