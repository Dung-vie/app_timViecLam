package vn.edu.tdc.apptimvieclam.aplication

import android.app.Application
import vn.edu.tdc.apptimvieclam.utils.NotificationHelper

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Khoi tao Channel cho Notification
        NotificationHelper.createNotificationChannel(this)
    }
}