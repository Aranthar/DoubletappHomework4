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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.doubletapphomework4.R
import com.example.doubletapphomework4.ui.common.enums.HabitFieldType
import com.example.doubletapphomework4.ui.common.enums.HabitPeriod
import com.example.doubletapphomework4.ui.common.enums.HabitPriority
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.screens.task_editor.models.TaskEditorEvent

class TaskEditorScreen(
    private val viewModel: TaskEditorViewModel,
    private val habitData: HabitUI? = null,
    private val onBackClicked: () -> Unit,
    private val onSaveChange: (HabitUI) -> Unit,
) {
    private var expandedPriority = mutableStateOf(false)
    private var expandedPeriod = mutableStateOf(false)

    @Composable
    fun Create() {
        val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
        val selectedPriorityOption = remember { mutableStateOf(viewState.habitData.priority) }
        val selectedPeriodOption = remember { mutableStateOf(viewState.habitData.period) }
        val radioButtonState =
            remember { mutableStateOf(viewState.habitData.type == HabitType.GOOD) }

        if (habitData != null) {
            LaunchedEffect(key1 = Unit) {
                habitData.let { viewModel.obtainEvent(TaskEditorEvent.UploadHabit(it)) }
            }
        }

        Surface {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
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

                HabitTypeRadioGroup(
                    radioButtonState = radioButtonState,
                    setSelectedHabitType = {
                        viewModel.obtainEvent(viewEvent = TaskEditorEvent.ChangeHabitType(it))
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .padding(top = 14.dp)
                        .fillMaxWidth()
                ) {
                    DropdownMenu(
                        selectedPeriodOption = selectedPeriodOption,
                        modifier = Modifier.weight(1f),
                        textId = R.string.period,
                        onChangeItem = {
                            viewModel.obtainEvent(
                                TaskEditorEvent.ChangePeriod(period = selectedPeriodOption.value)
                            )
                        }
                    )

                    DropdownMenu(
                        selectedPriorityOption = selectedPriorityOption,
                        modifier = Modifier.weight(1f),
                        textId = R.string.priority,
                        onChangeItem = {
                            viewModel.obtainEvent(
                                TaskEditorEvent.ChangePriority(priority = selectedPriorityOption.value)
                            )
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
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
        habitData: HabitUI,
    ) {
        Column {
            HabitFieldType.entries.forEach { fieldType ->
                TextField(
                    value = when (fieldType) {
                        HabitFieldType.TITLE -> habitData.title
                        HabitFieldType.DESCRIPTION -> habitData.description
                        HabitFieldType.REPEAT_COUNT -> habitData.repeatCount
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
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun DropdownMenu(
        selectedPriorityOption: MutableState<HabitPriority>? = null,
        selectedPeriodOption: MutableState<HabitPeriod>? = null,
        textId: Int,
        onChangeItem: () -> Unit,
        modifier: Modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(textId))

            if (selectedPriorityOption?.value != null) {
                Box {
                    Button(onClick = { expandedPriority.value = true }) {
                        Text(text = selectedPriorityOption.value.toString())
                    }
                }

                androidx.compose.material3.DropdownMenu(
                    expanded = expandedPriority.value,
                    onDismissRequest = { expandedPriority.value = false }
                ) {
                    HabitPriority.entries.forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(text = priority.name) },
                            onClick = {
                                selectedPriorityOption.value = priority
                                expandedPriority.value = false
                                onChangeItem()
                            }
                        )
                    }
                }
            } else {
                Box {
                    Button(onClick = { expandedPeriod.value = true }) {
                        Text(text = selectedPeriodOption?.value.toString())
                    }
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = expandedPeriod.value,
                    onDismissRequest = { expandedPeriod.value = false }
                ) {
                    HabitPeriod.entries.forEach { period ->
                        DropdownMenuItem(
                            text = { Text(text = period.name) },
                            onClick = {
                                selectedPeriodOption?.value = period
                                expandedPeriod.value = false
                                onChangeItem()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitTypeRadioGroup(
    radioButtonState: MutableState<Boolean>,
    setSelectedHabitType: (HabitType) -> Unit,
) {
    Column {
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