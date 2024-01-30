package com.example.taskmanager.screens.support

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.viewmodels.AllTasksViewModel

@Composable
fun MyBottomAppBarPager(
    supportSelectedMode: SupportSelectedModeForPager,
    allTasksViewModel: AllTasksViewModel = viewModel()
) {
    BottomAppBar {
        IconButton(onClick = {
            allTasksViewModel
                .deleteSelectedItems(supportSelectedMode.getSelectedWorks())
            supportSelectedMode.offSelectedMode()
        }) {
            Icon(Icons.Filled.Delete, contentDescription = "")
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            supportSelectedMode.offSelectedMode()
            supportSelectedMode.cancelAllSelections()
        }) {
            Icon(Icons.Filled.Close, "")
        }
    }
}

@Preview
@Composable
fun MyBottomAppBarPagerPreview() {
    val supportSelectedMode = SupportSelectedModeForPager()
    MyBottomAppBarPager(
        supportSelectedMode
    )
}
