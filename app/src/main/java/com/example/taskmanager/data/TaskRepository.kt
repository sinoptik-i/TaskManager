package com.example.taskmanager.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TaskRepository @Inject constructor(
     val dao: TaskDao,
     val scope: CoroutineScope
)  {
     fun getAllTasks() = dao.getAllTasks()

     fun getUnfinishedTasks() = dao.getUnfinishedTasks()
     fun getFinishedTasks()=dao.getFinishedTasks()

    //  fun getWastedWorks()


     suspend fun add(task: Task) =
        scope.async {
            return@async dao.add(task)
        }.await()

    fun delete(task: Task)=
        scope.launch {
            dao.delete(task)
        }

     suspend fun getTaskById(id: Int) = dao.getTaskById(id)
     suspend fun trackTaskById(id: Int) = dao.trackTaskById(id)
}