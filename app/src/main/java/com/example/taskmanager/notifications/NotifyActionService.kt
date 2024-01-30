package com.example.taskmanager.notifications

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.WorkManager
import com.example.taskmanager.DATE_FORMAT
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject


@AndroidEntryPoint
class NotifyActionService : Service() {

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }


    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var taskNotification: TaskNotification

    @Inject
    lateinit var workOperator: WorkOperator

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val id = intent.getIntExtra(TaskNotification.NOTIFICATION_ID_KEY, -1)
        val intentType = intent.getIntExtra(TaskNotification.ACTION_TYPE, -1)
        when (intentType) {
            TaskNotification.ADDITION_PREFIX_COMPLETE_PI -> completeWork(id)
            TaskNotification.ADDITION_PREFIX_WASTE_PI -> wasteWork(id)
        }
        taskNotification.deleteNotification(id)
        workOperator.deleteNotification(id)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun completeWork(id: Int) {
        scope.launch {
            val oldWork = taskRepository.getTaskById(id)
            if (oldWork != null) {
                val newWork = oldWork.copy(finished = true)
                taskRepository.add(newWork)
            }
        }
    }

    private fun wasteWork(id: Int) {
        scope.launch {
            val oldWork = taskRepository.getTaskById(id)
            if (oldWork != null) {
                val newWork = oldWork.copy(
                    finished = false,
                    attemptCount = oldWork.attemptCount + 1,
                    completionTime = getWastedTime()
                )
                taskRepository.add(newWork)
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun getWastedTime(): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        val date = Calendar.getInstance()

        date.set(Calendar.MINUTE, date.time.minutes - 2)
        return dateFormat.format(date.timeInMillis)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

}