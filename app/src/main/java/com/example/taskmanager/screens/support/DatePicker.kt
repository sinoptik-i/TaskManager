package com.example.taskmanager.screens.support

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import com.example.taskmanager.getNowDate
import java.text.SimpleDateFormat
import java.util.Calendar


@Composable
fun DatePicker(
    modifier: Modifier,
    date: MutableState<String>
) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    if (date.value == "") date.value = dateFormat.format(mCalendar.timeInMillis)
    // mCalendar.set(mYear, mMonth, mDay)


    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->

            mCalendar.set(mYear, mMonth, mDayOfMonth)
            date.value = dateFormat.format(mCalendar.timeInMillis)

        }, mYear, mMonth, mDay
    )
    Row(
        modifier = modifier,
        /* Modifier
             .padding(horizontal = 4.dp)
             .border(
                 BorderStroke(1.dp, Color.Black),
                 RoundedCornerShape(15)
             )        ,*/
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = "Дата: ${if (date.value == "") getNowDate() else date.value}"
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .width(150.dp)
                .padding(horizontal = 6.dp),
            onClick = {
                mDatePickerDialog.show()
            }

        ) {
            Text(
                text = "Выбрать"
            )
        }
    }
}


@Preview
@Composable
fun DatePickerPreview() {
    val date = remember {
        mutableStateOf("HH.mm")
    }
    DatePicker(modifier = Modifier, date = date)
}