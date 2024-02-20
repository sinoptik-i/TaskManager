package com.example.taskmanager.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.data.Task
import com.example.taskmanager.screens.allTasks.ALL_TASKS_VIEW
import com.example.taskmanager.screens.allTasks.AllTasksView


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
        fun backToATV(){
            navController.navigate(ALL_TASKS_VIEW) {
                popUpTo(ALL_TASKS_VIEW) {
                    inclusive = true
                }
            }
        }
        composable(ALL_TASKS_VIEW) {
            AllTasksView(
                onItemSelect = { selectedTask ->
                    currentTask = selectedTask
                },
                swapToOneItemView = {
                    navController.navigate(ONE_FULL_TASK)
                },
                onBackPressedForDrawer = {
                    backToATV()
                }
            )
        }
        composable(ONE_FULL_TASK) {

            OneFullTask(
                task = currentTask,
                onContinueClicked = {
                 //   backToATV()
                    navController.navigate(ALL_TASKS_VIEW) {
                        popUpTo(ALL_TASKS_VIEW) {
                            inclusive = true
                        }
                    }
                })
        }
    }
}