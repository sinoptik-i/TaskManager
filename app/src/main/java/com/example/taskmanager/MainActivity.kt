package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.taskmanager.notifications.TaskNotification
import com.example.taskmanager.screens.Navigation
import com.example.taskmanager.ui.theme.TaskManagerTheme
import com.example.taskmanager.viewmodels.OneTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val oneTaskViewModel: OneTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskFromNotificationId = intent.getIntExtra(TaskNotification.NOTIFICATION_ID_KEY, -1)
        if (taskFromNotificationId != -1) {
            oneTaskViewModel.setCurrentItemFromNotification(taskFromNotificationId)
        }

        setContent {
            TaskManagerTheme {
              Navigation()
            }
        }
    }
}

