package vn.edu.tdc.apptimvieclam.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.edu.tdc.apptimvieclam.adapters.NotificationAdapter
import vn.edu.tdc.apptimvieclam.databinding.NotificationLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Notification

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: NotificationLayoutBinding
    private lateinit var adapter: NotificationAdapter
    private val notifications = ArrayList<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotificationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotificationAdapter(this, notifications)
        binding.listNotification.adapter = adapter

        loadNotifications()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadNotifications() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            FirebaseDatabase
                .getInstance()
                .reference
                .child("notifications")
                .child(uid)
                .addValueEventListener(
                    object : ValueEventListener {

                        override fun onDataChange(
                            snapshot: DataSnapshot
                        ) {

                            notifications.clear()

                            for (item in snapshot.children) {

                                val notification =
                                    item.getValue(
                                        Notification::class.java
                                    )

                                if (notification != null) {

                                    notifications.add(
                                        notification
                                    )
                                }
                            }

                            notifications.reverse()

                            adapter.notifyDataSetChanged()

                            binding.layoutEmpty.visibility =
                                if (notifications.isEmpty())
                                    View.VISIBLE
                                else
                                    View.GONE
                        }

                        override fun onCancelled(
                            error: DatabaseError
                        ) {
                        }
                    }
                )
        }

    }
}