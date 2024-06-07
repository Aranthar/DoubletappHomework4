package com.example.doubletapphomework4.tests.ui_tests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.doubletapphomework4.MainActivity
import com.example.doubletapphomework4.ui.common.enums.HabitPeriod
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.common.repository.HabitRepository
import com.example.doubletapphomework4.ui.common.repository.HabitRepositoryImpl
import com.example.doubletapphomework4.ui.screens.task_editor.TaskEditorScreen
import com.example.doubletapphomework4.ui.screens.task_editor.TaskEditorViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

//@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TaskEditorScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: TaskEditorViewModel
    private lateinit var habitRepository: HabitRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        habitRepository = Mockito.mock(HabitRepository::class.java)
        val habitRepositoryImpl = HabitRepositoryImpl(habitRepository)
        viewModel = TaskEditorViewModel(habitRepositoryImpl)
    }

    @Test
    fun testTextFieldsInput() {
        composeTestRule.setContent {
            TaskEditorScreen(
                viewModel = viewModel,
                onBackClicked = {},
                onSaveChange = {}
            ).Create()
        }

        composeTestRule.onNodeWithText("Название привычки").performTextInput("Test Habit")
        composeTestRule.onNodeWithText("Описание привычки").performTextInput("Test Description")
        composeTestRule.onNodeWithText("Количество выполнения").performTextInput("3")

        composeTestRule.onNodeWithText("Test Habit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
    }

    @Test
    fun testRadioButtonSelection() {
        composeTestRule.setContent {
            TaskEditorScreen(
                viewModel = viewModel,
                onBackClicked = {},
                onSaveChange = {}
            ).Create()
        }

        composeTestRule.onNodeWithText(HabitType.GOOD.toString()).performClick()
        composeTestRule.onNodeWithText(HabitType.GOOD.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(HabitType.BAD.toString()).performClick()
        composeTestRule.onNodeWithText(HabitType.BAD.toString()).assertIsDisplayed()
    }

    @Test
    fun testDropdownMenuSelection() {
        composeTestRule.setContent {
            TaskEditorScreen(
                viewModel = viewModel,
                onBackClicked = {},
                onSaveChange = {}
            ).Create()
        }

        // Period Dropdown Menu
        composeTestRule.onNodeWithText("Period").performClick()
        composeTestRule.onNodeWithText(HabitPeriod.WEEK.toString()).performClick()
        composeTestRule.onNodeWithText(HabitPeriod.WEEK.toString()).assertIsDisplayed()

        // Priority Dropdown Menu
        composeTestRule.onNodeWithText("Priority").performClick()
        composeTestRule.onNodeWithText(HabitPriority.MEDIUM.toString()).performClick()
        composeTestRule.onNodeWithText(HabitPriority.MEDIUM.toString()).assertIsDisplayed()
    }

    @Test
    fun testButtonClick() {
        var backClicked = false
        var habitSaved: HabitUI? = null

        composeTestRule.setContent {
            TaskEditorScreen(
                viewModel = viewModel,
                onBackClicked = { backClicked = true },
                onSaveChange = { habitSaved = it }
            ).Create()
        }

        // Click Back Button
        composeTestRule.onNodeWithText("Назад").performClick()
        assert(backClicked)

        // Click Save Button
        composeTestRule.onNodeWithText("Сохранить изменения").performClick()
        assert(habitSaved != null)
    }
}