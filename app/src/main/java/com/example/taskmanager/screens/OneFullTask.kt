package com.example.taskmanager.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.screens.support.DateTimePicker
import com.example.taskmanager.screens.support.IOTextField
import com.example.taskmanager.viewmodels.OneTaskViewModel

const val ONE_FULL_TASK = "ONE_FULL_TASK"

@Composable
fun OneFullTask(
    task: Task,
    onContinueClicked: () -> Unit,
    oneTaskViewModel: OneTaskViewModel = hiltViewModel()
) {
    var currentTask=task

    val textTitle = rememberSaveable {
        mutableStateOf(currentTask.title)
    }


    val textDescription = rememberSaveable {
         mutableStateOf(currentTask.description)
    }
    val date = rememberSaveable {
       mutableStateOf(currentTask.completionTime.split(' ').first())
    }
    val time = rememberSaveable {
       mutableStateOf(currentTask.completionTime.split(' ').last())
    }

    val reminderTime = rememberSaveable {
        mutableStateOf(
            if (currentTask.reminderTime == "") {
                "00.03"
            } else {
                currentTask.reminderTime
            }
        )
    }

    val refreshCurrentTask = {
        currentTask =
            currentTask.copy(
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
            oneTaskViewModel.tryToAddTask(currentTask)
            onContinueClicked()
        }
        val completeWork = {
            refreshCurrentTask()
            oneTaskViewModel.completeTask(currentTask)
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
        )
        {
            Text("Попыток: ${currentTask.attemptCount}")
        }

        CompletedButton(completeWork)

        //  DescriptionTextField
        IOTextField(
            text = textDescription,
            labelText = "Description:"
        )
    }
}

@Composable
fun ButtonOk(
     addOrUpdateWork:()->Unit
){
    IconButton(
        onClick = addOrUpdateWork,
        modifier = Modifier
            .size(60.dp)
            .border(2.dp, MaterialTheme.colors.error, CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = null
        )
    }
}



@Composable
fun CompletedButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(
            horizontal = 4.dp
        ),
        onClick = onClick
    ) {
        Text(text = "Сделано")
    }
}


@Preview
@Composable
fun CompletedPreview() {
    CompletedButton({})
}
