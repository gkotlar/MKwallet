package com.example.mkwallet.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build

import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mkwallet.MainActivity
import com.example.mkwallet.R

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val taskData: Data = getInputData()
        val runtype: String = taskData.getString("run_type").toString()
        showNotification("NotificationWorker", "Message has been sent")

        val outputData: Data = Data.Builder().putString(WORK_RESULT, "Jobs finished").build()
        return Result.success(outputData)
    }

    private fun showNotification(title: String, desc: String) {
        val manager =
            getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "primary_channel"
        val channelName = "primary"
        var channel: NotificationChannel? = null


        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_menu_camera)
        manager.notify(1, builder.build())
    }

    companion object {
        private const val WORK_RESULT = "work_result"
    }
}