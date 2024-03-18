package com.example.taskmanager.screens.support

import android.graphics.Color
import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.screens.allTasks.SearchState
import com.example.taskmanager.viewmodels.AllTasksViewModel
import kotlinx.coroutines.launch


@Composable
fun TopBar(
    scaffoldState: ScaffoldState,
    searchState: MutableState<SearchState>,
    allTasksViewModel: AllTasksViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    TopAppBar {
        if (searchState.value == SearchState.RESULTS) {
            IconButton(onClick = {
                scope.launch {
                    searchState.value = SearchState.OFF
                    allTasksViewModel.setSearchArgs("")
                }
            }) {
                Icon(Icons.Filled.Close, "")
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        searchState.value = SearchState.INPUT
                    },
                text = allTasksViewModel.getQuery(),
                color= Color.White
            )
        }
        else {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        IconButton(onClick = {
            searchState.value = SearchState.INPUT
        }) {
            Icon(Icons.Filled.Search, "")
        }
    }
}

