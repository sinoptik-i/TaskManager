package com.example.taskmanager.notifications

import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.taskmanager.data.Task
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkOperator @Inject constructor(
    val workManager: WorkManager,
) {
    val TAG = this.javaClass.simpleName

    fun addNotifications(task: Task) {
        if (!task.isMissed()) {
            val workDataRemind = Data.Builder()
                .putString(ReminderWorker.TITLE_KEY, task.title)
                .putString(ReminderWorker.REMINDER_TIME_KEY, task.reminderTime)
                .putInt(TaskNotification.NOTIFICATION_ID_KEY, task.id)
                .build()

            val workDataComplete = Data.Builder()
                .putString(ReminderWorker.TITLE_KEY, task.title)
                .putInt(TaskNotification.NOTIFICATION_ID_KEY, task.id)
                .build()

            val diffCompletionTimeInMinutes = task.diffCompletionTimeInMinutes()
            val workRequestCompletion = OneTimeWorkRequestBuilder<ReminderWorker>(
            )
                .setInitialDelay(diffCompletionTimeInMinutes, TimeUnit.MINUTES)
                .setInputData(workDataComplete)
                .build()

            val diffReminderTimeInMinutes = task.diffReminderTimeInMinutes()
            val workRequestReminder = OneTimeWorkRequestBuilder<ReminderWorker>(
            )
                .setInitialDelay(diffReminderTimeInMinutes, TimeUnit.MINUTES)
                .setInputData(workDataRemind)
                .build()

            workManager.enqueueUniqueWork(

                task.reminderTaskId(),
                ExistingWorkPolicy.REPLACE,
                workRequestReminder
            )

            workManager.enqueueUniqueWork(
                task.completionTaskId(),
                ExistingWorkPolicy.REPLACE,
                workRequestCompletion
            )
            Log.e(TAG, " works id: ${task.id} added")
        }
    }

    fun deleteNotification(task: Task){
        workManager.cancelUniqueWork(task.completionTaskId())
        workManager.cancelUniqueWork(task.reminderTaskId())
    }

    fun deleteNotification(id: Int){
        workManager.cancelUniqueWork(Task.reminderWorkId(id))
        workManager.cancelUniqueWork(Task.completionWorkId(id))
    }

}