package vn.edu.tdc.apptimvieclam.admin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import vn.edu.tdc.apptimvieclam.admin.adapters.ApplicantAdapter
import vn.edu.tdc.apptimvieclam.databinding.ApplicantListLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Applications

class ApplicantListActivity : AppCompatActivity() {

    private lateinit var binding: ApplicantListLayoutBinding
    private lateinit var adapter: ApplicantAdapter

    private val applicantList = mutableListOf<Applications>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ApplicantListLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ApplicantAdapter(
            applicantList,

            onAccept = { application ->
                updateStatus(
                    application.applicationId,
                    "Accepted"
                )
            },

            onReject = { application ->
                updateStatus(
                    application.applicationId,
                    "Rejected"
                )
            }
        )

        binding.recyclerViewApplicants.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerViewApplicants.adapter = adapter

        loadApplicants()
    }

    // ham update trang thai khi employer duyet/tuchoi don
    private fun updateStatus(
        applicationId: String,
        status: String
    ) {

        FirebaseDatabase.getInstance()
            .getReference("applications")
            .child(applicationId)
            .child("status")
            .setValue(status)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    if (status == "Accepted")
                        "Duyệt thành công"
                    else
                        "Đã từ chối ứng viên",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadApplicants() {

        FirebaseDatabase.getInstance()
            .getReference("applications")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    applicantList.clear()

                    for (child in snapshot.children) {

                        val applicant =
                            child.getValue(Applications::class.java)

                        applicant?.let {
                            applicantList.add(it)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}