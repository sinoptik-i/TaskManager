package com.example.taskmanager.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.screens.support.MyBottomAppBarPager
import com.example.taskmanager.screens.support.SearchAlertDialog
import com.example.taskmanager.screens.support.SupportSelectedModeForPager
import com.example.taskmanager.screens.support.TopBar
import com.example.taskmanager.viewmodels.AllTasksViewModel
import kotlinx.coroutines.launch

const val ALL_TASKS_VIEW = "ALL_TASKS_VIEW"

enum class SearchState { OFF, INPUT, RESULTS }

@Composable
fun AllTasksView(
    onItemSelect: (task: Task) -> Unit,
    swapToOneItemView: () -> Unit,
    allTasksViewModel: AllTasksViewModel= hiltViewModel()
) {

    val searchState = rememberSaveable { mutableStateOf(SearchState.OFF) }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val supportSelectedMode by remember { mutableStateOf(SupportSelectedModeForPager()) }


    val closeDrawer = fun() {
        scope.launch {
            scaffoldState.drawerState.close()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                scaffoldState = scaffoldState,
                searchState = searchState
            )
        },

        bottomBar = {
            if (supportSelectedMode.selectedMode.value) {
                MyBottomAppBarPager(supportSelectedMode)
            }
        },
        /*drawerContent = {
            DrawerMenu(
                onBack = onBackForDrawer
            )
        }*/)
    { padding ->
        if (searchState.value == SearchState.INPUT) {
            SearchAlertDialog(searchState)
        }
        val createNewItem = {
           // onItemSelect(Task(0))
            onItemSelect(allTasksViewModel.getExampleItems(1)[0])
            swapToOneItemView()
        }
        Box(
            Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    if (supportSelectedMode.selectedMode.value) {
                        supportSelectedMode.selectedMode.value = false
                    }
                }
                .padding()
        ) {
            AllTasksPage(
                onItemSelect,
                { swapToOneItemView() },
                supportSelectedMode
            )
            ButtonAdd(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                isGone = supportSelectedMode.selectedMode.value,
                onClick = createNewItem
            )
        }
    }
}

@Composable
fun ButtonAdd(
    modifier: Modifier,
    isGone: Boolean,
    onClick: () -> Unit
) {
    if (!isGone) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
    }
}

