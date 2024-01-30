package com.example.taskmanager.screens.support

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.R


val timeDataPickerModifier2 = Modifier
    .padding(horizontal = 4.dp)
    .border(
        BorderStroke(1.dp, Color.Black),
        RoundedCornerShape(10)
    )
val timeDataPickerModifier = Modifier
    .padding(horizontal = 4.dp)
    .border(
        BorderStroke(1.dp, Color.Black),
        RoundedCornerShape(15)
    )

@Composable
fun DateTimePicker(
    time: MutableState<String>,
    date: MutableState<String>,
    label: String
) {

    Column(
        modifier = timeDataPickerModifier2.padding(top = 4.dp, bottom = 4.dp)
    ) {
        Row()
        {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = label)
            Spacer(modifier = Modifier.weight(1f))
        }
        DatePicker(modifier = timeDataPickerModifier, date = date)
        TimePicker(modifier = timeDataPickerModifier, time = time)
    }

}
@Preview
@Composable
fun DateTimePickerDTPreview() {
    val date = remember {
        mutableStateOf("dd.MM.yyyy")
    }
    val time = remember {
        mutableStateOf("hh.mm")
    }
    val label = stringResource(R.string.select_date_time)
    DateTimePicker(time, date, label)
}


@Composable
fun DateTimePicker(
    time: MutableState<String>,
    label: String
) {
    Column(
        modifier = timeDataPickerModifier2.padding(top = 4.dp, bottom = 4.dp)
    ) {
        Row()
        {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = label)
            Spacer(modifier = Modifier.weight(1f))
        }
        TimePicker(modifier = timeDataPickerModifier, time = time)
    }
}
@Preview
@Composable
fun DateTimePickerTPreview() {
    val date = remember {
        mutableStateOf("dd.MM.yyyy")
    }
    val time = remember {
        mutableStateOf("HH.mm")
    }
    val label = stringResource(R.string.select_time)
    DateTimePicker(time,  label)
}