package com.example.taskmanager.screens.support

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.allTasks.SearchState
import com.example.taskmanager.viewmodels.AllTasksViewModel

@Composable
fun SearchAlertDialog(
    searchState: MutableState<SearchState>,
    allTasksViewModel: AllTasksViewModel = hiltViewModel()
) {
    val searchArgTitle = rememberSaveable {
        mutableStateOf(allTasksViewModel.getQuery())
    }
    AlertDialog(onDismissRequest = {
        if (allTasksViewModel.getQuery() == "") {
            searchState.value = SearchState.OFF
        } else {
            searchState.value = SearchState.RESULTS
        }
    },
        buttons = {
            Column(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                TitleTextField(title = searchArgTitle)
                Button(modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.End),
                    onClick = {
                        allTasksViewModel.setSearchArgs(searchArgTitle.value)
                        if (searchArgTitle.value != "") {
                            searchState.value = SearchState.RESULTS
                        }
                    }) {
                    Text(text = "Search")
                }
            }
        }
    )
}