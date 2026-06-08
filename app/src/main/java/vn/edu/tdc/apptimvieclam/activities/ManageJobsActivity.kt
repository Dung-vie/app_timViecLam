package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import vn.edu.tdc.apptimvieclam.adapters.ManageJobAdapter
import vn.edu.tdc.apptimvieclam.databinding.ActivityManageJobsBinding
import vn.edu.tdc.apptimvieclam.models.Job

class ManageJobsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageJobsBinding
    private lateinit var adapter: ManageJobAdapter
    private val jobList = mutableListOf<Job>()
    private val dbRef = FirebaseDatabase.getInstance().getReference("jobs")
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Setup RecyclerView
        adapter = ManageJobAdapter(
            jobList,
            onEdit = { job ->
                val intent = Intent(this, EditJobActivity::class.java)
                intent.putExtra("jobId", job.id)
                startActivity(intent)
            },
            onDelete = { job -> deleteJob(job) }
        )
        binding.rvJobs.layoutManager = LinearLayoutManager(this)
        binding.rvJobs.adapter = adapter

        // FAB -> mở màn thêm tin
        binding.fabAddJob.setOnClickListener {
            startActivity(Intent(this, CreateJobActivity::class.java))
        }

        loadJobs()
    }

    override fun onResume() {
        super.onResume()
        loadJobs()
    }

    private fun loadJobs() {
        binding.progressBar.visibility = View.VISIBLE
        dbRef.orderByChild("userId").equalTo(currentUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    jobList.clear()
                    for (child in snapshot.children) {
                        val job = child.getValue(Job::class.java)
                        if (job != null) jobList.add(job)
                    }
                    jobList.reverse()
                    adapter.notifyDataSetChanged()
                    binding.tvTotalJobs.text = jobList.size.toString()
                    binding.layoutEmpty.visibility = if (jobList.isEmpty()) View.VISIBLE else View.GONE
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    // ham xóa job

    private fun deleteJob(job: Job) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa tin \"${job.title}\" không?")
            .setPositiveButton("Xóa") { _, _ ->
                dbRef.child(job.id).removeValue()
                    .addOnSuccessListener { loadJobs() }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}