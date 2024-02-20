package com.example.taskmanager.screens.drawer

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.support.ButtonRow
import com.example.taskmanager.viewmodels.AllTasksViewModel
import com.example.taskmanager.viewmodels.AuthViewModel

@Composable
fun SignedScreen(
    allTasksViewModel: AllTasksViewModel = hiltViewModel(),
    authViewModel: AuthViewModel= hiltViewModel()
) {
    ButtonRow(text = "Upload works") {
        allTasksViewModel.delAndUploadCloud()
    }

    ButtonRow(text = "Download works") {
        allTasksViewModel.downloadAllItemsFromCloud()
    }
    ButtonRow(text = "Clear Cloud Db") {
        allTasksViewModel.clearCloudDb()
    }
    ButtonRow(text = "Log out") {
        authViewModel.logOut()
    }
}