package com.example.taskmanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.notifications.WorkOperator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContentState<T : Any>(
    val content: T? = null,
    val isInProgres: Boolean = false,
    val error: Throwable? = null
) {

}

@HiltViewModel
class OneTaskViewModel @Inject constructor(
    val repository: TaskRepository,
    val workOperator: WorkOperator
) : ViewModel() {

    val TAG = this.javaClass.simpleName

    private val taskIdFlow = MutableStateFlow<Int?>(null)
    val state: StateFlow<ContentState<Task>> =
        taskIdFlow.flatMapLatest { id ->
            id?.let {
                flow {
                    emit(ContentState(isInProgres = true))
                    try {
                        getTaskByIdScope(id)
                            .let {
                                delay(5000)
                                emit(ContentState(it)) }
                    } catch (error: Throwable) {
                        emit(ContentState(error = error))
                    }

                }
            } ?: flowOf(ContentState(content = Task(0)))
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                ContentState(isInProgres = true)
            )

   // var task = Task(0)


    fun setTask(id: Int) {
        taskIdFlow.value = id
    }

    suspend fun getTaskByIdScope(id: Int) = viewModelScope
        .async(Dispatchers.IO) {
            return@async repository.getTaskById(id)
        }.await() ?: Task(id = 0)

    private fun addNotifications(task: Task) {
        workOperator.addNotifications(task)
    }

    fun tryToAddTask(task: Task): Boolean {
        if (task.title != "") {
            viewModelScope.launch {
                val id = repository.add(
                    task
                ).toInt()
                //если создалась новая таска, до добавления в бд id=0
                // @PrimaryKey(autoGenerate = true) сам создает нужный id
                val newTask = task.copy(id = id)
                addNotifications(
                    newTask
                )
            }
            return true
        }
        /*else if (task.description == "") {
            return false
        }*/
        return false
    }


    fun completeTask(task: Task) {
        workOperator.deleteNotification(task.id)
        val newTask = task.copy(finished = true)
        tryToAddTask(newTask)
    }
}