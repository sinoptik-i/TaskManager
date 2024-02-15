package com.example.taskmanager.screens.support

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.viewmodels.AllTasksViewModel


@Composable
fun DrawerMenu(
    onBack: () -> Unit = {},
    allTasksViewModel: AllTasksViewModel = hiltViewModel()
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
}

