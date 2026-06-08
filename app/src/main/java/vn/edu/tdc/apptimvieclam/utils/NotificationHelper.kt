package vn.edu.tdc.apptimvieclam.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import vn.edu.tdc.apptimvieclam.R

object NotificationHelper {
    const val CHANNEL_ID = "notification_example"
    const val CHANNEL_NAME = "Day la kenh cho cac vi du notification"

    fun createNotificationChannel(context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Day la kenh vi du cho cac notification"
                enableVibration(true)
                enableLights(true)
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, message: String) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        NotificationManagerCompat
            .from(context)
            .notify(System.currentTimeMillis().toInt(), builder.build())
    }
}