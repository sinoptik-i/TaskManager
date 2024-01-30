package com.example.taskmanager.notifications

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    val TAG = this.javaClass.simpleName

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val REMINDER_TIME_KEY = "REMINDER_TIME_KEY"
    }

    /* @Inject
     lateinit var workNotification: WorkNotification*/
    val taskNotification = TaskNotification(context)

    override fun doWork(): Result {
        val title = inputData.getString(TITLE_KEY)
        val reminderTime = inputData.getString(REMINDER_TIME_KEY)
        val notificationId = inputData.getInt(TaskNotification.NOTIFICATION_ID_KEY, -1)

        if (title != null && notificationId != -1) {
            if (reminderTime != null) {
                taskNotification.startNotification(
                    "Внимание-внимание!",
                    "Через $reminderTime $title",
                    notificationId
                )
                Log.e(TAG, "reminder Notif $reminderTime")
            } else {
                taskNotification.startNotification(
                    "Внимание-внимание!",
                    title,
                    notificationId
                )
                Log.e(TAG, "complete Notif ")
            }
        }
        return Result.success()
    }
}
