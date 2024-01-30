package com.example.taskmanager.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.viewmodels.OneTaskViewModel



@Composable
fun Navigation(
    oneTaskViewModel: OneTaskViewModel = hiltViewModel()
) {
    val TAG ="Navigation"
    val navController = rememberNavController()
    /*val currentTask = remember {
        mutableStateOf(oneTaskViewModel.currentTask)
    }*/
    NavHost(
        navController = navController,
        startDestination =
        if (oneTaskViewModel.currentTaskIsEmpty()) {
            ALL_TASKS_VIEW
        } else {
            ONE_FULL_TASK
        }
    ) {
        composable(ALL_TASKS_VIEW) {
          /* if( navController.popBackStack()){
               navController.clearBackStack(ALL_TASKS_VIEW)
           }

            Log.e(TAG,"${it.destination.route}")
            Log.e(TAG,"${it.arguments}")
*/
            AllTasksView(
                onItemSelect = {
                   oneTaskViewModel.currentTask = it
                },
                swapToOneItemView = {
                    navController.navigate(ONE_FULL_TASK)
                }
            )
        }
        composable(ONE_FULL_TASK) {
//            Log.e(TAG,"${it.destination.route}")
            OneFullTask(
                //task = currentTask.value,
                onContinueClicked = {
                    navController.navigate(ALL_TASKS_VIEW)
                })
        }
    }
}