package com.example.doubletapphomework4.ui.screens.task_editor

import androidx.lifecycle.viewModelScope
import com.example.doubletapphomework4.ui.common.enums.HabitFieldType
import com.example.doubletapphomework4.ui.common.models.BaseViewModel
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.common.repository.HabitRepositoryImpl
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorEvent
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditorViewModel @Inject constructor(private val habitRepositoryImpl: HabitRepositoryImpl) :
    BaseViewModel<TaskEditorViewState, TaskEditorEvent>(initialState = TaskEditorViewState()) {
    private var habitData: HabitUI = HabitUI()
    private var uploadHabit: Boolean = false

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

            is TaskEditorEvent.ChangePeriod -> {
                habitData = habitData.copy(period = viewEvent.period)

                viewState.update {
                    it.copy(habitData = habitData)
                }
            }

            is TaskEditorEvent.ChangeFieldText -> {
                habitData = when (viewEvent.type) {
                    HabitFieldType.TITLE -> habitData.copy(title = viewEvent.text)
                    HabitFieldType.DESCRIPTION -> habitData.copy(description = viewEvent.text)
                    HabitFieldType.REPEAT_COUNT -> habitData.copy(repeatCount = viewEvent.text)
                }

                viewState.update { it.copy(habitData = habitData) }
            }

            TaskEditorEvent.ClickBackBtn -> setHabitAndUpdateViewState()

            TaskEditorEvent.ClickBtnSave -> {
                viewModelScope.launch {
                    viewState.update { it.copy(habitData = habitData.copy()) }

                    if (uploadHabit) {
                        habitRepositoryImpl.updateHabit(habitData)
                    } else habitRepositoryImpl.insertHabit(habitData)

                    uploadHabit = false
                }
            }

            is TaskEditorEvent.UploadHabit -> {
                setHabitAndUpdateViewState(viewEvent.habitData.copy())
                uploadHabit = true
            }

            TaskEditorEvent.ClearViewState -> setHabitAndUpdateViewState()
        }
    }

    private fun setHabitAndUpdateViewState(habitData: HabitUI = HabitUI()) {
        this.habitData = habitData
        viewState.update { it.copy(habitData = habitData) }
        uploadHabit = false
    }
}