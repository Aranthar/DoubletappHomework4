package com.example.doubletapphomework4.ui.screens.tasks_list

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.doubletapphomework4.R
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListEvent
import com.example.doubletapphomework4.ui.screens.tasks_list.views.HabitCard
import kotlinx.coroutines.launch

class TasksListScreen(
    private val viewModel: TasksListViewModel,
    private val habit: HabitData?,
    private val onCreateCard: () -> Unit,
    private val onHabitClick: (HabitData) -> Unit,
) {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun Create() {
        val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { HabitType.entries.size })
        val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

        if (habit != null) {
            LaunchedEffect(key1 = Unit) {
                habit.let { viewModel.obtainEvent(TasksListEvent.UploadHabit(it)) }
            }
        }

        Scaffold(
            topBar = { TopAppBar(title = { Text(text = stringResource(R.string.habit_list)) }) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    onCreateCard()
                }) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add))
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex.value,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HabitType.entries.forEachIndexed { index, habitType ->
                        Tab(
                            selected = selectedTabIndex.value == index,
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.outline,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(habitType.ordinal)
                                }
                            },
                            text = { Text(text = habitType.name) }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        HabitList(
                            habitsList = viewState.habitsByType[
                                HabitType.getFromOrdinal(selectedTabIndex.value)
                            ]!!,
                            onHabitClick = onHabitClick
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun HabitList(
        habitsList: List<HabitData>,
        onHabitClick: (HabitData) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            items(habitsList.size) { index ->
                HabitCard(
                    habitData = habitsList[index],
                    onHabitClick = onHabitClick
                )
                if (index != habitsList.size - 1) Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}