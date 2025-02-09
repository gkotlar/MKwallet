package com.example.mkwallet.services

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mkwallet.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class NotificationWorker (context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val taskData: Data = getInputData()
        val oznaka : String = taskData.getString("oznaka").toString()
        val rate : Double = taskData.getDouble("rate", 0.0)
        val formater = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var localDate = LocalDate.now()

        //var exchangeRates = repository.getExchangeRates(startDate = currentDate.value.toString(), endDate = currentDate.value.toString()).asLiveData()
        //var rate2 = repository.getSredenByOznaka(oznaka)
        //if (rate2 > rate){
        //   showNotification(String.format(Locale.GERMAN, "%s has reached a higher value than  %0.02f", rate), "Open the app tho check for more")
        // }

        val outputData: Data = Data.Builder().putString(WORK_RESULT, "Jobs finished").build()
        return Result.success(outputData)
    }

    private fun showNotification(title: String, desc: String) {
        val manager =
            getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "primary_channel"
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