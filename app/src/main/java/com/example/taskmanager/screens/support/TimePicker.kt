package com.example.taskmanager.screens.support

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar


@SuppressLint("SimpleDateFormat")
@Composable
fun TimePicker(
    modifier: Modifier,
    time: MutableState<String>
) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    var mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    var mMinute = mCalendar.get(Calendar.MINUTE)

    val timeFormat = SimpleDateFormat(TIME_FORMAT)
    if (time.value == "") {
        time.value = timeFormat.format(Calendar.getInstance().timeInMillis)
        //time.value="00.05"
    } else {
        mHour = time.value.split('.')[0].toInt()
        mMinute = time.value.split('.')[1].toInt()
    }

//        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
    val mDatePickerDialog = TimePickerDialog(
        mContext,
        TimePickerDialog.THEME_HOLO_LIGHT,
        { _: TimePicker, mHour: Int, mMinute: Int ->

            mCalendar.set(Calendar.MINUTE, mMinute)
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour)
            time.value = timeFormat.format(mCalendar.timeInMillis)

        }, mHour, mMinute, true
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = "Время: ${time.value}"//${if (time.value == "") getNowTime() else time.value}"
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            //   modifier = Modifier.weight(5f),
            modifier = Modifier
                .width(150.dp)
                .padding(horizontal = 6.dp),
            onClick = {
                mDatePickerDialog.show()
            }

        ) {
            Text("Выбрать")
        }
    }
}

@Preview
@Composable
fun TimePickerPreview() {

    val time = remember { mutableStateOf("") }
    TimePicker(
        Modifier, time
    )
}