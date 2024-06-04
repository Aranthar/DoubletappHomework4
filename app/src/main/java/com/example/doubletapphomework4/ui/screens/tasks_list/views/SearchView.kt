package com.example.doubletapphomework4.ui.screens.tasks_list.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.doubletapphomework4.R
import com.example.doubletapphomework4.utils.clearFocusOnKeyboardDismiss

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    restoredValue: String,
    onClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    var value by remember { mutableStateOf(restoredValue) }
    LaunchedEffect(key1 = restoredValue) { value = restoredValue }
    val enabled = onClick == null

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        BasicTextField(
            modifier = Modifier.clearFocusOnKeyboardDismiss(),
            value = value,
            onValueChange = {
                if (enabled) {
                    value = it
                    onValueChange(it)
                }
            },
            enabled = enabled,
            singleLine = true,
            textStyle = TextStyle.Default.copy(color = Color.White),
        ) { innerTextField ->
            DecorationBox(
                textValue = value,
                modifier = if (enabled) Modifier else Modifier.clickable(
                    onClick = onClick!!,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = Color.White)
                ),
                enabled = enabled,
                trailingIconId = R.drawable.ic_search,
                innerTextField = innerTextField,
            )
        }
    }
}


@Composable
private fun DecorationBox(
    textValue: String,
    modifier: Modifier,
    enabled: Boolean = true,
    trailingIconId: Int? = null,
    innerTextField: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .background(color = Color.Gray)
            .padding(vertical = 10.dp, horizontal = 15.dp), // Inner padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (textValue.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.search_hint),
                    color = Color.White
                )
            }
            if (enabled) innerTextField()
        }

        if (trailingIconId == null) return

        Icon(
            painter = painterResource(trailingIconId),
            contentDescription = "icon",
            tint = Color.DarkGray
        )
    }
}