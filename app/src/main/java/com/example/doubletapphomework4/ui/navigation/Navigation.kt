package com.example.doubletapphomework4.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.screens.about_us.AboutUsScreen
import com.example.doubletapphomework4.ui.screens.task_editor.TaskEditorScreen
import com.example.doubletapphomework4.ui.screens.task_editor.TaskEditorViewModel
import com.example.doubletapphomework4.ui.screens.tasks_list.TasksListViewModel
import com.example.doubletapphomework4.ui.screens.tasks_list.views.Drawer

class Navigation {
    private var navController: NavHostController? = null
    private var habit: HabitUI? = null
    private var isEdit = false

    @Composable
    fun Create() {
        navController = rememberNavController()
        val taskListViewModel = hiltViewModel<TasksListViewModel>()
        val taskEditorViewModel = hiltViewModel<TaskEditorViewModel>()

        NavHost(navController = navController!!, startDestination = TASKS_LIST_ROUTE) {
            composable(TASK_EDITOR_ROUTE) {
                CreateTaskEditorScreen(taskEditorViewModel, habit)
            }
            composable(TASKS_LIST_ROUTE) {
                CreateTasksListScreen(taskListViewModel, habit)
            }
            composable(ABOUT_US_ROUTE) {
                AboutUsScreen()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun CreateTasksListScreen(
        taskListViewModel: TasksListViewModel,
        habit: HabitUI?,
    ) {
        Drawer(
            viewModel = taskListViewModel,
            onCreateCard = {
                isEdit = false
                navController!!.navigate(TASK_EDITOR_ROUTE)
            },
            habit = habit,
            onHabitClick = {
                isEdit = true
                this.habit = it
                navController!!.navigate(TASK_EDITOR_ROUTE)
            },
            onDrawerClick = {
                if (it == "О нас") {
                    navController!!.navigate(ABOUT_US_ROUTE)
                } else {
                    navController!!.navigate(TASKS_LIST_ROUTE)
                }
            }
        )
    }

    @Composable
    private fun CreateTaskEditorScreen(
        taskEditorViewModel: TaskEditorViewModel,
        habit: HabitUI?,
    ) {
        TaskEditorScreen(
            viewModel = taskEditorViewModel,
            habitData = if (isEdit) habit else null,
            onSaveChange = {
                this.habit = it
                navController!!.navigate(TASKS_LIST_ROUTE)
            },
            onBackClicked = {
                navController!!.popBackStack()
            }
        ).Create()
    }

    companion object {
        const val TASKS_LIST_ROUTE = "TASKS_LIST_ROUTE"
        const val TASK_EDITOR_ROUTE = "TASK_EDITOR_ROUTE"
        const val ABOUT_US_ROUTE = "ABOUT_US_ROUTE"
    }
}