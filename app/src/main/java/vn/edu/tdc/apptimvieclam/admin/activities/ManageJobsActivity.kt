package vn.edu.tdc.apptimvieclam.admin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.edu.tdc.apptimvieclam.admin.adapters.JobApprovalAdapter
import vn.edu.tdc.apptimvieclam.databinding.AdminManageJobsLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company

class ManageJobsActivity : AppCompatActivity() {
    private lateinit var binding: AdminManageJobsLayoutBinding
    private val jobs = ArrayList<Company>()
    private lateinit var adapter: JobApprovalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AdminManageJobsLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        adapter = JobApprovalAdapter(this, jobs)

        binding.lvJobs.adapter = adapter
        loadJobs()
    }

    private fun loadJobs() {

        FirebaseDatabase
            .getInstance()
            .reference
            .child("jobs")
            .addValueEventListener(
                object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        jobs.clear()

                        for (item in snapshot.children) {

                            val company = item.getValue(Company::class.java)

                            if (
                                company != null &&
                                company.status.equals("PENDING", true)
                            ) {
                                jobs.add(company)
                            }
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }
            )
    }
}