package com.example.taskmanager.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.support.DateTimePicker
import com.example.taskmanager.screens.support.IOTextField
import com.example.taskmanager.viewmodels.OneTaskViewModel
import com.example.taskmanager.viewmodels.OneTaskViewModelCurrentTask

const val ONE_FULL_TASK_CT="ONE_FULL_TASK_CT"
@Composable
fun OneFullTaskCT(
    onContinueClicked: () -> Unit,
    oneTaskViewModel: OneTaskViewModelCurrentTask = hiltViewModel()
) {

    val textTitle = remember {
        mutableStateOf(oneTaskViewModel.currentTask.title)
    }

    val textDescription = remember {
        mutableStateOf(oneTaskViewModel.currentTask.description)
    }

    val date = remember {
        mutableStateOf(oneTaskViewModel.currentTask.completionTime.split(' ').first())
    }

    val time = remember {
        mutableStateOf(oneTaskViewModel.currentTask.completionTime.split(' ').last())
    }

    val reminderTime = remember {
        mutableStateOf(
            if (oneTaskViewModel.currentTask.reminderTime == "") {
                "00.03"
            } else {
                oneTaskViewModel.currentTask.reminderTime
            }
        )
    }

    val refreshCurrentTask = {
        oneTaskViewModel.currentTask =
            oneTaskViewModel.currentTask.copy(
                title = textTitle.value,
                description = textDescription.value,
                completionTime = "${date.value} ${time.value}",
                reminderTime = reminderTime.value
            )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        val addOrUpdateWork = {
            refreshCurrentTask()
            oneTaskViewModel.tryToAddTask()
            onContinueClicked()
        }
        val completeWork = {
            refreshCurrentTask()
            oneTaskViewModel.completeTask()
            onContinueClicked()
        }

        ButtonOk {
            addOrUpdateWork()
        }

        IOTextField(
            text = textTitle,
            labelText = "Title:"
        )
        val labelDateTime = "Выбрать дату и время:"
        DateTimePicker(time, date, labelDateTime)

        val labelReminderTime = "Напомнить за:"
        DateTimePicker(reminderTime, labelReminderTime)
        Row(
            Modifier
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Попыток: ${oneTaskViewModel.currentTask.attemptCount}")
        }

        CompletedButton(completeWork)

        //  DescriptionTextField
        IOTextField(
            text = textDescription,
            labelText = "Description:"
        )
    }
}