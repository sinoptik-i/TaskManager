package com.example.taskmanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.notifications.WorkOperator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@HiltViewModel
class OneTaskViewModel @Inject constructor(
    val repository: TaskRepository,
    val workOperator: WorkOperator
) : ViewModel() {

    val TAG = this.javaClass.simpleName

    var currentTask = Task(id = 0)// by mutableStateOf(Task(id = 0))

    fun currentTaskIsEmpty() = currentTask.id == 0


    fun setCurrentItemFromNotification(id: Int): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e(TAG, "id $id")
            Log.e(TAG, "${currentTask.id} ${currentTask.title}")
            currentTask = getWorkById(id) ?: currentTask
            Log.e(TAG, "${currentTask.id} ${currentTask.title}")
        }
        return false
    }

    suspend fun getWorkById(id: Int): Task? = repository.getTaskById(id)


    private fun addNotifications(task: Task) {
        workOperator.addNotifications(task)

    }

    fun tryToAddTask(): Boolean {
        if (currentTask.title != "") {
            viewModelScope.launch {
                val id = repository.add(
                    currentTask
                ).toInt()
                this@OneTaskViewModel.currentTask = currentTask.copy(id = id)
                addNotifications(
                    this@OneTaskViewModel.currentTask
                )
            }
            return true
        } else if (currentTask.description == "") {
            return false
        }
        return true
    }


    fun completeTask() {
        workOperator.deleteNotification(currentTask.id)
        currentTask = currentTask.copy(finished = true)
        tryToAddTask()
    }
}