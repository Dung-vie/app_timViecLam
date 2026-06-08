package vn.edu.tdc.apptimvieclam.utils

import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.models.Notification

object FirebaseNotification {

    fun addNotification(
        uid: String,
        notification: Notification
    ) {

        FirebaseDatabase
            .getInstance()
            .reference
            .child("notifications")
            .child(uid)
            .push()
            .setValue(notification)
    }
}