package vn.edu.tdc.apptimvieclam.activities

import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.databinding.JobDetailLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import vn.edu.tdc.apptimvieclam.models.Applications
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import vn.edu.tdc.apptimvieclam.R

class JobDetailActivity : AppCompatActivity() {

    private lateinit var binding: JobDetailLayoutBinding

    private var isSaved = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = JobDetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val company = intent.getSerializableExtra("JOB") as? Company

        if (company != null) {
            loadData(company)
        }

        binding.btnApplyNow.setOnClickListener {
            applyJob(company)
        }
        // bat su kien cho nut back
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Bắt sự kiện lưu công việc
        binding.ivBookmark.setOnClickListener {

            isSaved = !isSaved

            if (isSaved) {

                binding.ivBookmark.setImageResource(
                    R.drawable.ic_bookmark_filled
                )

                saveJob(company)

            } else {

                binding.ivBookmark.setImageResource(
                    R.drawable.ic_bookmark_border
                )
            }
        }
    }

    // ==========================================
    // HÀM APPLY JOB (Đã cập nhật kiểm tra trùng)
    // ==========================================
    private fun applyJob(company: Company?) {
        if (company == null) return

        // 1. Kiểm tra đăng nhập
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để ứng tuyển!", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUserId = currentUser.uid
        val currentUserEmail = currentUser.email ?: ""

        // Lấy thông tin từ object company truyền qua Intent
        // LƯU Ý: Giả sử model Company của bạn có trường .id (nếu tên biến là jobId hay gì khác thì bạn đổi lại nhé)
        val currentJobId = company.id
        val currentJobTitle = company.title ?: ""
        val currentCompany = company.company?.name ?: ""

        val database = FirebaseDatabase.getInstance().getReference("applications")

        // 2. Kiểm tra chống Apply trùng
        database.orderByChild("userId").equalTo(currentUserId).get()
            .addOnSuccessListener { snapshot ->
                var isAlreadyApplied = false

                // Duyệt qua các đơn cũ để xem có trùng ID công việc không
                for (child in snapshot.children) {
                    val app = child.getValue(Applications::class.java)
                    if (app?.jobId == currentJobId) {
                        isAlreadyApplied = true
                        break
                    }
                }

                // 3. Xử lý kết quả
                if (isAlreadyApplied) {
                    Toast.makeText(this, "Bạn đã ứng tuyển công việc này rồi!", Toast.LENGTH_SHORT).show()
                } else {
                    // Chưa Apply -> Tạo mới và lưu lên Firebase
                    val applicationId = database.push().key ?: return@addOnSuccessListener
                    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                    // Tạo dữ liệu chuẩn theo Model Applications
                    val newApplication = Applications(
                        applicantName = currentUserEmail.substringBefore("@"), // Lấy tạm phần đầu email làm tên
                        email = currentUserEmail,
                        jobTitle = currentJobTitle,
                        company = currentCompany,
                        applyDate = currentDate,
                        applicationId = applicationId,
                        userId = currentUserId,
                        jobId = currentJobId,
                        status = "Pending"
                    )

                    database.child(applicationId).setValue(newApplication)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Ứng tuyển thành công!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Ứng tuyển thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Lỗi kết nối với máy chủ", Toast.LENGTH_SHORT).show()
            }
    }

    // ==========================================
    // HÀM LOAD DATA LÊN GIAO DIỆN
    // ==========================================
    private fun loadData(company: Company) {
        // Job title
        binding.tvJobTitle.text = company.title
        // Company
        binding.tvCompany.text = company.company?.name
        // Location
        binding.tvLocation.text = company.location
        // Salary
        binding.tvSalary.text =
            if (company.salaryMin != null && company.salaryMax != null) {
                "${company.salaryMin} - ${company.salaryMax}"
            } else {
                "Thương lượng"
            }
        // Job type
        binding.tvJobType.text =
            if (company.types.isNotEmpty())
                company.types[0].nameType
            else
                "Không"

        // Position
        binding.tvPosition.text =
            when (company.experienceLevel) {
                "EN" -> "Entry Level"
                "SE" -> "Senior"
                else -> company.experienceLevel
            }

        binding.tvDescription.text =
            HtmlCompat.fromHtml(
                company.description ?: "",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )

        Glide.with(this)
            .load(company.company?.image)
            .into(binding.ivCompanyLogo)
    }
    // hàm lưu job
    private fun saveJob(company: Company?) {

        if (company == null) return

        val database = FirebaseDatabase.getInstance()

        val savedRef = database.getReference("saved_jobs")

        val jobId = savedRef.push().key

        val jobData = hashMapOf(
            "title" to company.title,
            "company" to company.company.name,
            "location" to company.location,
            "description" to company.description
        )

        jobId?.let {
            savedRef.child(it).setValue(jobData)
        }
    }

}