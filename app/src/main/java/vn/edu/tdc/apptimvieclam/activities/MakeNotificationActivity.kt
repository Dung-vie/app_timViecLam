//package vn.edu.tdc.apptimvieclam.activities
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.PendingIntent
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//
//class MakeNotificationActivity : AppCompatActivity() {
//    private lateinit var binding: NotificationLayoutBinding
//    private val REQ = 999
//    private var notificationID = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = NotificationLayoutBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        if (checkPermission(Manifest.permission.POST_NOTIFICATIONS)) {
//            //Cho phep
//            doSomething()
//        }
//        // Cho phép doSomething()
//        else {
//            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQ)
//        }
//    }
//
//    // Ham dung de post mot notification len he thong
//    @SuppressLint("MissingPermission")
//    private fun postNotification(posID: Int) {
//        //Log.d("notification", "Called")
//        val builder = NotificationCompat.Builder(
//            this,
//            NotificationHelper.CHANNEL_ID
//        )
//            .setSmallIcon(R.drawable.alarm_foreground)
//            .setContentTitle("Canh bao!")
//            .setContentText("Day la Notification vi du!")
//            .setPriority(NotificationCompat. PRIORITY_HIGH)
//            .setAutoCancel(true)
//        val manager = NotificationManagerCompat.from(this)
//        manager.notify(posID, builder.build())
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun postNotificationWithOpenApp(posID: Int) {
//        val intent = Intent(this, MakeNotificationActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//        //Tao doi tuong notification nhu binh thuong
//        val notification = NotificationCompat.Builder(
//            this,
//            NotificationHelper.CHANNEL_ID
//        )
//            .setSmallIcon(R.drawable.alarm_foreground)
//            .setContentTitle("Canh bao loai 2")
//            .setContentText("Hay nhap chon de mo lai ung dung!")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .build()
//
//        val manager = NotificationManagerCompat.from(this)
//        manager.notify(posID, notification)
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun postNotificationWithOrtherApp(posID: Int) {
//        val targetPackage = "vn.edu.tdc.service2026"
//        val targetPackageMap = "com.google.android.apps.maps"
//        val lauchIntent = packageManager.getLaunchIntentForPackage(targetPackage)
//        if (lauchIntent != null) {
//            lauchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            val pendingIntent = PendingIntent.getActivity(
//                this, 0 , lauchIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            val notification = NotificationCompat.Builder(
//                this,
//                NotificationHelper.CHANNEL_ID
//            )
//                .setSmallIcon(R.drawable.alarm_foreground)
//                .setContentTitle("Canh bao mo ung dung")
//                .setContentText("Hay vao day de mo ung dung khac!")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .build()
//
//            val manager = NotificationManagerCompat.from(this)
//            manager.notify(posID, notification)
//        }
//        else {
//            Log.d("test", "Ung dung chua duoc cai dat trong dien thoai!")
//        }
//    }
//
//    private fun checkPermission(permission: String): Boolean {
//        return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQ && permissions.size == grantResults.size && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            doSomething()
//        }
//    }
//
//    private fun doSomething() {
//        //Bat su kien cho nut button
//        binding.btnPost.setOnClickListener {
////            postNotification(notificationID++)
////            postNotificationWithOpenApp(notificationID++)
//            postNotificationWithOrtherApp(notificationID++)
//        }
//    }
//}