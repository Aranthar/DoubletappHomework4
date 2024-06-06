package com.example.doubletapphomework4.ui.screens.tasks_list.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.doubletapphomework4.R
import com.example.doubletapphomework4.ui.common.enums.HabitType
import com.example.doubletapphomework4.ui.common.models.HabitUI

@Composable
fun HabitCard(
    habitData: HabitUI,
    onHabitClick: (HabitUI) -> Unit,
    onHabitDone: () -> Unit,
    onHabitDelete: () -> Unit,
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onHabitClick(habitData) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ColoredCircle(
                            color = if (habitData.type == HabitType.GOOD) {
                                Color.Green
                            } else Color.Red
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = habitData.priority.toString())
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = habitData.title)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = habitData.description)
                    }
                }

                IconButton(onClick = onHabitDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete habit")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Тип привычки: ${habitData.type}")

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Количество раз: ${habitData.repeatCount}",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Периодичность: ${habitData.period}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = onHabitDone,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.done))
            }
        }
    }
}

@Composable
fun ColoredCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color)
    )
}