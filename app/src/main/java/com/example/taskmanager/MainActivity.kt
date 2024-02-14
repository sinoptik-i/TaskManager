package com.example.taskmanager

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.taskmanager.notifications.TaskNotification
import com.example.taskmanager.screens.Navigation
import com.example.taskmanager.screens.support.Progress
import com.example.taskmanager.ui.theme.TaskManagerTheme
import com.example.taskmanager.viewmodels.OneTaskViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val oneTaskViewModel: OneTaskViewModel by viewModels()
    private val TAG = this.javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskFromNotificationId = intent
            .getIntExtra(TaskNotification.NOTIFICATION_ID_KEY, -1)
        if (taskFromNotificationId != -1) {
            oneTaskViewModel.setTask(taskFromNotificationId)
        }

        setContent {
            val contentState by oneTaskViewModel.state.collectAsState()
            val (task, isInProgress, error) = contentState
            TaskManagerTheme {
                when {
                    task != null -> Navigation(task)
                    isInProgress -> Progress()// CircularProgressIndicator()
                    error != null -> {
                        Toast.makeText(
                            this,
                            error.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }
}

