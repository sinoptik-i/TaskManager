package com.example.taskmanager.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.data.Task
import com.example.taskmanager.viewmodels.OneTaskViewModel
import com.example.taskmanager.viewmodels.OneTaskViewModelCurrentTask


@Composable
fun NavigationCT(
    oneTaskViewModel: OneTaskViewModelCurrentTask = hiltViewModel()
) {


    val TAG = "Navigation"
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination =
        if (oneTaskViewModel.currentTaskIsEmpty()) {
            ALL_TASKS_VIEW
        } else {
            ONE_FULL_TASK_CT
        }
    ) {
        composable(ALL_TASKS_VIEW) {
            AllTasksView(
                onItemSelect = { selectedTask ->
                    oneTaskViewModel.currentTask = selectedTask
                },
                swapToOneItemView = {
                    navController.navigate(ONE_FULL_TASK_CT)
                }
            )
        }
        composable(ONE_FULL_TASK_CT) {
            OneFullTaskCT(
                onContinueClicked = {
                    navController.popBackStack()
                })
        }
    }
}