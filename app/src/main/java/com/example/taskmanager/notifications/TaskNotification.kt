package com.example.taskmanager.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class TaskNotification @Inject constructor(
   @ApplicationContext val context: Context
) {

    private val CHANNEL_ID = "CHANNEL_ID"


    companion object {
        const val NOTIFICATION_ID_KEY = "NOTIFICATION_ID"
        const val ADDITION_PREFIX_CLICK_PI=1000
        const val ADDITION_PREFIX_COMPLETE_PI=2000
        const val ADDITION_PREFIX_WASTE_PI=3000
        const val ACTION_TYPE= "ACTION_TYPE"
    }



    fun deleteNotification(id: Int) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(id)
    }


    private fun createClickPendingIntent(id: Int): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(NOTIFICATION_ID_KEY, id)
        //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context,
            ADDITION_PREFIX_CLICK_PI +id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }

    private fun createActionCompletePendingIntent(id:Int): PendingIntent? {
        val intent= Intent(context, NotifyActionService::class.java)
        intent.putExtra(NOTIFICATION_ID_KEY, id)
        intent.putExtra(ACTION_TYPE, ADDITION_PREFIX_COMPLETE_PI)
        val pendingIntent= PendingIntent.getService(
            context,
            ADDITION_PREFIX_COMPLETE_PI +id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }

    private fun createActionWastePendingIntent(id:Int): PendingIntent? {
        val intent= Intent(context, NotifyActionService::class.java)
        intent.putExtra(NOTIFICATION_ID_KEY, id)
        intent.putExtra(ACTION_TYPE, ADDITION_PREFIX_WASTE_PI)
        val pendingIntent= PendingIntent.getService(
            context,
            ADDITION_PREFIX_WASTE_PI +id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }


    fun startNotification(title: String, text: String, id: Int) {

        val notificationManager = NotificationManagerCompat.from(context)

        val onClickPendingIntent = createClickPendingIntent(id)

        val actionComplete= NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_foreground,
            "Completed!",
            createActionCompletePendingIntent(id)
        ).build()

        val actionWaste= NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_foreground,
            "Wasted",
            createActionWastePendingIntent(id)
        ).build()

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            // .setWhen(System.currentTimeMillis())
            .setContentIntent(onClickPendingIntent)
            .setContentTitle(title)
            .setContentText(text)
            .addAction(actionComplete)
            .addAction(actionWaste)
            .setPriority(Notification.PRIORITY_HIGH)

        createChannelIfNeeded(notificationManager)


        /*  if (ActivityCompat.checkSelfPermission(
                  context,
                  Manifest.permission.POST_NOTIFICATIONS
              ) != PackageManager.PERMISSION_GRANTED
          ) {
              return
          }*/
        notificationManager.notify(id, notificationBuilder.build())
    }

    fun createChannelIfNeeded(manager: NotificationManagerCompat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(notificationChannel)
        }
    }


}