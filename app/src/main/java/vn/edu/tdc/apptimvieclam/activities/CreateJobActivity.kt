package vn.edu.tdc.apptimvieclam.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.databinding.ActivityCreateJobBinding
import vn.edu.tdc.apptimvieclam.models.Job

class CreateJobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateJobBinding
    private val dbRef = FirebaseDatabase.getInstance().getReference("jobs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Tạm thời chưa hỗ trợ upload logo
        binding.btnUploadLogo.isEnabled = false
        binding.btnUploadLogo.text = "Chưa hỗ trợ"

        binding.btnSave.setOnClickListener {
            if (validate()) {
                saveJob()
            }
        }
    }

    // ─── Validate ────────────────────────────────────────────────────────────

    private fun validate(): Boolean {
        var isValid = true

        val title = binding.edtTitle.text.toString().trim()
        val companyName = binding.edtCompanyName.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        if (title.isEmpty()) {
            binding.tilTitle.error = "Không được để trống"
            isValid = false
        } else binding.tilTitle.error = null

        if (companyName.isEmpty()) {
            binding.tilCompanyName.error = "Không được để trống"
            isValid = false
        } else binding.tilCompanyName.error = null

        if (address.isEmpty()) {
            binding.tilAddress.error = "Không được để trống"
            isValid = false
        } else binding.tilAddress.error = null

        if (binding.chipGroupJobType.checkedChipId == View.NO_ID) {
            binding.tvJobTypeError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvJobTypeError.visibility = View.GONE
        }

        if (description.isEmpty()) {
            binding.tilDescription.error = "Không được để trống"
            isValid = false
        } else binding.tilDescription.error = null

        return isValid
    }

    // ─── Lưu job lên Realtime Database ───────────────────────────────────────

    private fun saveJob() {
        setLoading(true)

        val title = binding.edtTitle.text.toString().trim()
        val companyName = binding.edtCompanyName.text.toString().trim()
        val location = binding.edtAddress.text.toString().trim()
        val salary = binding.edtSalary.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        val jobType = when (binding.chipGroupJobType.checkedChipId) {
            binding.chipFulltime.id -> "Full-time"
            binding.chipParttime.id -> "Part-time"
            binding.chipRemote.id -> "Remote"
            else -> ""
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val jobId = dbRef.push().key ?: run {
            setLoading(false)
            return
        }

        val job = Job(
            id = jobId,
            title = title,
            companyName = companyName,
            location = location,
            salary = salary,
            jobType = jobType,
            description = description,
            companyLogo = "",
            userId = userId,
            createdAt = System.currentTimeMillis()
        )

        dbRef.child(jobId).setValue(job)
            .addOnSuccessListener {
                setLoading(false)
                Toast.makeText(this, "Đăng tin thành công!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                setLoading(false)
                Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // ─── Hiển thị / ẩn loading ───────────────────────────────────────────────

    private fun setLoading(isLoading: Boolean) {
        binding.btnSave.isEnabled = !isLoading
        binding.btnSave.text = if (isLoading) "Đang xử lý..." else "Đăng tin"
    }
}