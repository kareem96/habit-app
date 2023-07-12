package com.dicoding.habitapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.detail.DetailHabitActivity
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK

class NotificationWorker(private val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val habitId = inputData.getInt(HABIT_ID, 0)
    private val habitTitle = inputData.getString(HABIT_TITLE)

    override fun doWork(): Result {
        val prefManager =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify =
            prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        //TODO 12 : If notification preference on, show notification with pending intent
        if (shouldNotify) {
            showNotification(ctx)
        } else Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show()

        return Result.success()
    }

    private fun showNotification(context: Context) {
        val intent = Intent(context, DetailHabitActivity::class.java).apply {
            putExtra(HABIT_ID, habitId)
        }
        val pendingFlags: Int = if (Build.VERSION.SDK_INT >= 23) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else PendingIntent.FLAG_UPDATE_CURRENT

        val pendingIntent = PendingIntent.getActivity(applicationContext, habitId, intent, pendingFlags)
        val mNotifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("TimesUps!")
            .setContentText(habitTitle)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIF_UNIQUE_WORK,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Habit Notification"
            mNotifManager.createNotificationChannel(channel)
        }
        mNotifManager.notify(habitId, notifBuilder.build())
    }
}
