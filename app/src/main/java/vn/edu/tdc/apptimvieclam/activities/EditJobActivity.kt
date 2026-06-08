package vn.edu.tdc.apptimvieclam.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.edu.tdc.apptimvieclam.databinding.ActivityEditJobBinding
import vn.edu.tdc.apptimvieclam.models.Job

class EditJobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditJobBinding
    private val dbRef = FirebaseDatabase.getInstance().getReference("jobs")
    private lateinit var jobId: String
    private val jobTypes = listOf("Full-time", "Part-time", "Remote")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        jobId = intent.getStringExtra("jobId") ?: run { finish(); return }

        binding.spJobType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            jobTypes
        )

        loadJob()

        binding.btnSave.setOnClickListener { saveChanges() }
    }

    private fun loadJob() {
        dbRef.child(jobId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val job = snapshot.getValue(Job::class.java) ?: return
                binding.edtTitle.setText(job.title)
                binding.edtLocation.setText(job.location)   // ← location
                binding.edtSalary.setText(job.salary)
                binding.edtDescription.setText(job.description)
                binding.spJobType.setSelection(
                    jobTypes.indexOf(job.jobType).coerceAtLeast(0)
                )
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun saveChanges() {
        val title = binding.edtTitle.text.toString().trim()
        val location = binding.edtLocation.text.toString().trim()  // ← đổi tên biến
        val salary = binding.edtSalary.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val jobType = binding.spJobType.selectedItem.toString()

        if (title.isEmpty()) { binding.tilTitle.error = "Không được để trống"; return }
        else binding.tilTitle.error = null

        if (location.isEmpty()) { binding.tilLocation.error = "Không được để trống"; return }
        else binding.tilLocation.error = null

        if (description.isEmpty()) { binding.tilDescription.error = "Không được để trống"; return }
        else binding.tilDescription.error = null

        val updates = mapOf(
            "title" to title,
            "location" to location,       // ← đổi "address" thành "location"
            "salary" to salary,
            "jobType" to jobType,
            "description" to description
        )

        binding.btnSave.isEnabled = false
        binding.btnSave.text = "Đang lưu..."

        dbRef.child(jobId).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
                binding.btnSave.isEnabled = true
                binding.btnSave.text = "Lưu thay đổi"
            }
    }
}