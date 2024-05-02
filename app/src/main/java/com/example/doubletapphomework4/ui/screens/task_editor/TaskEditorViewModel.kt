package com.example.doubletapphomework4.ui.screens.task_editor

import com.example.doubletapphomework4.ui.common.enums.HabitFieldType
import com.example.doubletapphomework4.ui.common.models.BaseViewModel
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorEvent
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TaskEditorViewModel @Inject constructor() :
    BaseViewModel<TaskEditorViewState, TaskEditorEvent>(initialState = TaskEditorViewState()) {
    private var habitData: HabitData = HabitData()

    override fun obtainEvent(viewEvent: TaskEditorEvent) {
        when (viewEvent) {
            is TaskEditorEvent.ChangeHabitType -> {
                habitData = habitData.copy(type = viewEvent.type)

                viewState.update {
                    it.copy(habitData = habitData)
                }
            }

            is TaskEditorEvent.ChangePriority -> {
                habitData = habitData.copy(priority = viewEvent.priority)

                viewState.update {
                    it.copy(habitData = habitData)
                }
            }

            is TaskEditorEvent.ChangeFieldText -> {
                habitData = when (viewEvent.type) {
                    HabitFieldType.TITLE -> habitData.copy(title = viewEvent.text)
                    HabitFieldType.DESCRIPTION -> habitData.copy(description = viewEvent.text)
                    HabitFieldType.REPEAT_COUNT -> habitData.copy(repeatCount = viewEvent.text)
                    HabitFieldType.PERIOD -> habitData.copy(period = viewEvent.text)
                }

                viewState.update { it.copy(habitData = habitData) }
            }

            TaskEditorEvent.ClickBackBtn -> setHabitAndUpdateViewState()

            TaskEditorEvent.ClickBtnSave -> {
                viewState.update { it.copy(habitData = habitData.copy()) }
            }

            is TaskEditorEvent.UploadHabit -> {
                setHabitAndUpdateViewState(viewEvent.habitData.copy())
            }

            TaskEditorEvent.ClearViewState -> setHabitAndUpdateViewState()
        }
    }

    private fun setHabitAndUpdateViewState(habitData: HabitData = HabitData()) {
        this.habitData = habitData
        viewState.update {
            it.copy(habitData = habitData)
        }
    }
}