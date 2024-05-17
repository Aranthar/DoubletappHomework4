package com.example.doubletapphomework4.ui.screens.tasks_list

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.doubletapphomework4.R
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitData
import com.example.doubletapphomework4.ui.screens.tasks_list.models.Filters
import com.example.doubletapphomework4.ui.screens.tasks_list.models.TasksListEvent
import com.example.doubletapphomework4.ui.screens.tasks_list.views.HabitCard
import com.example.doubletapphomework4.ui.screens.tasks_list.views.SearchView
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
        val selectedFilter = remember { mutableStateOf(viewState.selectedFilter) }

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
                SearchView(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                    restoredValue = "",
                    onClick = {
                        viewModel.obtainEvent(TasksListEvent.IsSheetOpen(true))
                    },
                    onValueChange = {}
                )

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

                BottomSheet(
                    searchText = viewState.searchText,
                    selectedFilter = selectedFilter,
                    showBottomSheet = viewState.showBottomSheet,
                    filteredList = viewState.filteredList
                )
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BottomSheet(
        searchText: String,
        selectedFilter: MutableState<Filters>,
        showBottomSheet: Boolean,
        filteredList: List<HabitData>,
    ) {
        val sheetState = rememberModalBottomSheetState()

        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    viewModel.obtainEvent(TasksListEvent.IsSheetOpen(false))
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 20.dp, bottom = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SearchView(modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.68f)
                            .padding(end = 6.dp),
                            restoredValue = searchText,
                            onValueChange = {
                                viewModel.obtainEvent(
                                    TasksListEvent.SearchCard(
                                        it,
                                        selectedFilter.value
                                    )
                                )
                            }
                        )

                        DropdownMenu(
                            selectedFilter = selectedFilter,
                            onChangeItem = {
                                viewModel.obtainEvent(
                                    TasksListEvent.SearchCard(
                                        searchText,
                                        selectedFilter.value
                                    )
                                )
                            },
                            modifier = Modifier.weight(0.32f)
                        )
                    }

                    HabitList(
                        habitsList = filteredList,
                        onHabitClick = {}
                    )
                }
            }
        }
    }

    @Composable
    private fun DropdownMenu(
        selectedFilter: MutableState<Filters>,
        onChangeItem: () -> Unit,
        modifier: Modifier,
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            Box {
                Button(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(text = selectedFilter.value.toString())
                }
            }

            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Filters.entries.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(text = filter.name) },
                        onClick = {
                            selectedFilter.value = filter
                            expanded = false
                            onChangeItem()
                        }
                    )
                }
            }
        }
    }
}