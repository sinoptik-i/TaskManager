package com.example.taskmanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.cloudDb.ICloudDb
import com.example.taskmanager.cloudDb.fireStore.FirestoreDB
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.getNowDate
import com.example.taskmanager.getNowTime
import com.example.taskmanager.getNowTimePlus3Min
import com.example.taskmanager.notifications.WorkOperator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val workOperator: WorkOperator,
    private val firestoreDB: FirestoreDB//ICloudDb
) : ViewModel() {
    val TAG = this.javaClass.simpleName

    fun getExampleItems(count: Int) = List(count) {
        Task(
            0,
            "title ${getNowTime()} ",
            "descr",
            "${getNowDate()} ${getNowTimePlus3Min()}",
            it % 2 != 0,
            0,
            ""
        )
    }

    fun delAndUploadCloud() = firestoreDB.uploadItems()

    fun downloadAllItemsFromCloud() = firestoreDB.downloadAllItems()

    fun clearCloudDb() = firestoreDB.clearDb()


    private fun getActualTasks() = taskRepository.getUnfinishedTasks().map { tasks ->
        tasks.filter { task ->
            !task.isMissed()
        }
    }

    private fun getWastedTasks() = taskRepository.getUnfinishedTasks().map { tasks ->
        tasks.filter { task ->
            task.isMissed()
        }
    }

    // отбирает таски в зависимости от выбранной вкладки
    private fun flowAllItemsForPager(pagerState: Int): Flow<List<Task>> {
        //     Log.e(TAG, "state: $pagerState")
        return when (pagerState) {
            0 -> getActualTasks()
            1 -> taskRepository.getFinishedTasks()
            2 -> getWastedTasks()
            else -> return taskRepository.getAllTasks()

        }
        /*  .onEach { works ->
              works.forEach { work ->
                  Log.e(
                      TAG, "id: ${work.id}, finished: ${work.finished}, " +
                              "completionTime: ${work.completionTime}, " +
                              "reminderTime: ${work.reminderTime}, " +
                              "state: $pagerState"
                  )
              }
          }*/
    }

    fun deleteSelectedItems(items: List<Task>) {
        items.forEach { task ->
            workOperator.deleteNotification(task.id)
            taskRepository.delete(task)
        }
    }
//---------------------------------------------


    fun setSearchArgs(searchArgTitle: String) {
        _query.value = searchArgTitle
    }


    val _query = MutableStateFlow("")
    fun getQuery() = _query.value

    fun flowAllItemsWithSearchForPager(pagerState: Int) = _query
        // .debounce(1000L)
        // .distinctUntilChanged()
        .combine(flowAllItemsForPager(pagerState)) { query, items ->
            if (query.isEmpty()) {
                items
            } else {
                items.filter {
//                    it.title.contains(query)
                    it.toString().contains(query)
                }
            }

        }.distinctUntilChanged()
}