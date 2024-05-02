package com.example.doubletapphomework4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.doubletapphomework4.ui.navigation.Navigation
import com.example.doubletapphomework4.ui.screens.tasks_list.TasksListScreen
import com.example.doubletapphomework4.ui.screens.tasks_list.TasksListViewModel
import com.example.doubletapphomework4.ui.theme.DoubletappHomework4Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DoubletappHomework4Theme {
                Navigation().Create()
            }
        }
    }
}