package com.example.doubletapphomework4.ui.screens.tasks_list.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doubletapphomework4.ui.common.models.HabitUI
import com.example.doubletapphomework4.ui.screens.tasks_list.TasksListScreen
import com.example.doubletapphomework4.ui.screens.tasks_list.TasksListViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Drawer(
    viewModel: TasksListViewModel,
    onCreateCard: () -> Unit,
    habit: HabitUI?,
    onHabitClick: (HabitUI) -> Unit,
    onDrawerClick: (item: String) -> Unit,
) {
    val items = listOf("Главный экран", "О нас")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column {
                Spacer(modifier = Modifier.padding(top = 40.dp))

                items.forEach { item ->
                    TextButton(
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                            onDrawerClick(item)
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.LightGray,
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(item, fontSize = 22.sp)
                    }
                }
            }
        },
        scrimColor = Color.DarkGray,
        content = {
            TasksListScreen(
                viewModel = viewModel,
                habit = habit,
                onCreateCard = onCreateCard,
                onHabitClick = onHabitClick
            ).Create()
        }
    )
}