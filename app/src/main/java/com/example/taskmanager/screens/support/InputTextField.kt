package com.example.taskmanager.screens.support

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IOTextField(
    text: MutableState<String>,
    labelText:String
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        value = text.value,
        onValueChange = {
            text.value = it
        },
        label = { Text(labelText) }
    )
}

@Composable
fun TitleTextField(title: MutableState<String>) {
    IOTextField(
        text = title,
        labelText = "Title:"
    )
}