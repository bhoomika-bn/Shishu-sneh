package com.mindmatrix.shishusneh.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mindmatrix.shishusneh.R

class VaccineNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        val channel = NotificationChannel(
            "vaccine_channel",
            "Vaccine Reminder",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(
            applicationContext,
            "vaccine_channel"
        )
            .setContentTitle("Vaccination Reminder")
            .setContentText("Your baby's vaccination is due soon")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)

        return Result.success()
    }
}