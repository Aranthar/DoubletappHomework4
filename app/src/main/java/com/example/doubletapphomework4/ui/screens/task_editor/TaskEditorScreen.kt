package com.example.doubletapphomework4.ui.screens.task_editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.doubletapphomework4.R
import com.example.doubletapphomework4.ui.common.enums.HabitFieldType
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorEvent
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorViewState
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListEvent

class TaskEditorScreen(
    private val viewModel: TaskEditorViewModel,
    private val habitData: HabitData? = null,
    private val onBackClicked: () -> Unit,
    private val onSaveChange: (HabitData) -> Unit,
) {
    private var expanded = mutableStateOf(false)

    @Composable
    fun Create() {
        val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
        val selectedOption = remember { mutableStateOf(viewState.habitData.priority) }
        val radioButtonState =
            remember { mutableStateOf(viewState.habitData.type == HabitType.GOOD) }

        if (habitData != null) {
            LaunchedEffect(key1 = Unit) {
                habitData.let { viewModel.obtainEvent(TaskEditorEvent.UploadHabit(it)) }
            }
        }

        Surface {
            Column(
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 20.dp)
                    .fillMaxSize()
            ) {
                TextFields(
                    onValueChange = { fieldType, text ->
                        viewModel.obtainEvent(
                            TaskEditorEvent.ChangeFieldText(fieldType, text)
                        )
                    },
                    habitData = viewState.habitData
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    DropdownMenu(
                        selectedOption = selectedOption,
                        modifier = Modifier.weight(1f),
                        onChangeItem = {
                            viewModel.obtainEvent(
                                TaskEditorEvent.ChangePriority(priority = selectedOption.value)
                            )
                        }
                    )
                    HabitTypeRadioGroup(
                        radioButtonState = radioButtonState,
                        modifier = Modifier.weight(1f),
                        setSelectedHabitType = {
                            viewModel.obtainEvent(viewEvent = TaskEditorEvent.ChangeHabitType(it))
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        onBackClicked()
                        viewModel.obtainEvent(TaskEditorEvent.ClickBackBtn)
                    }) {
                        Text(text = "Назад")
                    }

                    TextButton(onClick = {
                        viewModel.obtainEvent(TaskEditorEvent.ClickBtnSave)
                        onSaveChange(viewState.habitData)
                        viewModel.obtainEvent(TaskEditorEvent.ClearViewState)
                    }) {
                        Text(text = "Сохранить изменения")
                    }
                }
            }
        }
    }

    @Composable
    private fun TextFields(
        onValueChange: (fieldType: HabitFieldType, text: String) -> Unit,
        habitData: HabitData,
    ) {
        Column {
            HabitFieldType.entries.forEach { fieldType ->
                TextField(
                    value = when (fieldType) {
                        HabitFieldType.TITLE -> habitData.title
                        HabitFieldType.DESCRIPTION -> habitData.description
                        HabitFieldType.REPEAT_COUNT -> habitData.repeatCount
                        HabitFieldType.PERIOD -> habitData.period
                    },
                    modifier = Modifier
                        .padding(bottom = 14.dp)
                        .fillMaxWidth(),
                    onValueChange = {
                        onValueChange(fieldType, it)
                    },
                    label = {
                        when (fieldType) {
                            HabitFieldType.TITLE -> Text(text = "Название привычки")
                            HabitFieldType.DESCRIPTION -> Text(text = "Описание привычки")
                            HabitFieldType.REPEAT_COUNT -> Text("Количество выполнения")
                            HabitFieldType.PERIOD -> Text("Периодичность выполнения")
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun DropdownMenu(
        selectedOption: MutableState<HabitPriority>,
        onChangeItem: () -> Unit,
        modifier: Modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.priority))

            Box {
                Button(onClick = { expanded.value = true }) {
                    Text(text = selectedOption.value.toString())
                }
            }

            androidx.compose.material3.DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                HabitPriority.entries.forEach { priority ->
                    DropdownMenuItem(
                        text = { Text(text = priority.name) },
                        onClick = {
                            selectedOption.value = priority
                            expanded.value = false
                            onChangeItem()
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun HabitTypeRadioGroup(
        radioButtonState: MutableState<Boolean>,
        setSelectedHabitType: (HabitType) -> Unit,
        modifier: Modifier,
    ) {
        Column(modifier) {
            Text(text = stringResource(R.string.habit_type))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = radioButtonState.value,
                    onClick = {
                        radioButtonState.value = true
                        setSelectedHabitType(HabitType.GOOD)
                    }
                )
                Text(text = HabitType.GOOD.toString())
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !radioButtonState.value,
                    onClick = {
                        radioButtonState.value = false
                        setSelectedHabitType(HabitType.BAD)
                    }
                )
                Text(text = HabitType.BAD.toString())
            }

        }
    }
}