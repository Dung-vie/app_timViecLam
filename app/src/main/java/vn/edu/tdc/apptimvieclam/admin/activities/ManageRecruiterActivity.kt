package vn.edu.tdc.apptimvieclam.admin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.edu.tdc.apptimvieclam.admin.adapters.RecruiterAdapter
import vn.edu.tdc.apptimvieclam.databinding.AdminManageRecruiterLayoutBinding
import vn.edu.tdc.apptimvieclam.models.User

class ManageRecruiterActivity : AppCompatActivity() {
    private lateinit var binding: AdminManageRecruiterLayoutBinding
    private val recruiters = ArrayList<User>()
    private lateinit var adapter: RecruiterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminManageRecruiterLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        adapter = RecruiterAdapter(this, recruiters)

        binding.lvRecruiters.adapter = adapter
        loadRecruiters()
    }

    private fun loadRecruiters() {

        FirebaseDatabase
            .getInstance()
            .reference
            .child("users")
            .addValueEventListener(
                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        recruiters.clear()

                        for (item in snapshot.children) {

                            val user =
                                item.getValue(
                                    User::class.java
                                )

                            if (
                                user != null
                                && user.role == "employee"
                                && user.status == "pending"
                            ) {

                                recruiters.add(user)
                            }
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {

                    }
                }
            )
    }
}