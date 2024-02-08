package com.example.taskmanager.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.screens.support.SupportSelectedModeForPager
import com.example.taskmanager.viewmodels.AllTasksViewModel
import com.example.taskmanager.viewmodels.OneTaskViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

//const val
@OptIn(ExperimentalPagerApi::class)
@Composable
fun AllTasksPage(
    onItemSelect: (task: Task) -> Unit,
    swapToOneItemView: () -> Unit,
    supportSelectedModeForPager: SupportSelectedModeForPager    ,
 //   allTasksViewModel: AllTasksViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState()
    val tabs = listOf(
        "ActualTasks",
        "CompletedTasks",
        "WastedTasks",
        "All"
    )
    Column() {
        Tabs(tabs = tabs, pagerState = pagerState)
        AllTasks(
            onItemSelect,
            swapToOneItemView,
            pagerState = pagerState,
            supportSelectedModeForPager
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AllTasks(
    onItemSelect: (task: Task) -> Unit,
    swapToOneItemView: () -> Unit,
    pagerState: PagerState,
    supportSelectedModeForPager: SupportSelectedModeForPager,
    allTasksViewModel: AllTasksViewModel = hiltViewModel(),
    oneTaskViewModel: OneTaskViewModel= hiltViewModel()
) {
    HorizontalPager(

        state = pagerState,
        count = 4) { page ->
        val items by allTasksViewModel
            .flowAllItemsWithSearchForPager(pagerState.currentPage)
            .collectAsState(initial = emptyList())
        val mode by supportSelectedModeForPager.selectedMode
        supportSelectedModeForPager.setWorks(items)
        LazyColumn(
            Modifier
                .padding(top = 4.dp, bottom = 4.dp)
                .fillMaxSize()
        ) {
            items(items) { currentItem ->
                //KAK ETO OBRABOTAT'?
                val checkState by supportSelectedModeForPager.getCheckState(currentItem)
                val selectItem = {
                   // oneTaskViewModel.currentTask=currentItem
                    onItemSelect(currentItem)
                    swapToOneItemView()
                }
                TaskView(
                   task= currentItem,
                   selectedMode =  mode,//mode,//supportSelectedMode.selectedMode.value,
                    checkState= checkState,//checkState,
                    onCheckedChange = {
                        supportSelectedModeForPager.changeSelectOfItems(currentItem, it)
                    },
                    { selectItem() },
                    {
                        supportSelectedModeForPager.changeSelectedMode(currentItem)
                    }
                )

            }
        }
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<String>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = {
                    Text(
                        tab,
                        letterSpacing = 0.7.sp
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}
