package com.example.taskmanager.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.data.Task
import com.example.taskmanager.viewmodels.OneTaskViewModel


@Composable
fun Navigation(
    task: Task,
    //oneTaskViewModel: OneTaskViewModel = hiltViewModel()
) {


    val TAG = "Navigation"
    val navController = rememberNavController()

    var currentTask = task
    NavHost(
        navController = navController,
        startDestination =
        if (task.id == 0) {
            ALL_TASKS_VIEW
        } else {
            ONE_FULL_TASK
        }
    ) {
        composable(ALL_TASKS_VIEW) {
            AllTasksView(
                onItemSelect = { selectedTask ->
                    currentTask = selectedTask
                },
                swapToOneItemView = {
                    navController.navigate(ONE_FULL_TASK)
                }
            )
        }
        composable(ONE_FULL_TASK) {

            OneFullTask(
                task = currentTask,
                onContinueClicked = {
                    navController.navigate(ALL_TASKS_VIEW) {
                        popUpTo(ALL_TASKS_VIEW) {
                            inclusive = true
                        }
                    }
                })
        }
    }
}