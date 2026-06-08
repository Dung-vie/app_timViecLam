package vn.edu.tdc.apptimvieclam.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import vn.edu.tdc.apptimvieclam.databinding.JobDetailLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.R

class JobDetailActivity : AppCompatActivity() {

    private lateinit var binding: JobDetailLayoutBinding
    private var isSaved = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=JobDetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val company = intent.getSerializableExtra("JOB") as? Company

        if (company != null) {
            loadData(company)
        }

        binding.btnApplyNow.setOnClickListener {
            applyJob(company)
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

    // ham apply job
    private fun applyJob(company: Company?) {

        if (company == null) return

        val database = FirebaseDatabase.getInstance()
        val applicationsRef = database.getReference("applications")

        val applicationId = applicationsRef.push().key

        val applicationData = hashMapOf(
            "jobTitle" to company.title,
            "companyName" to company.company.name,
            "location" to company.location,
            "status" to "Pending"
        )

        if (applicationId != null) {
            applicationsRef.child(applicationId)
                .setValue(applicationData)
                .addOnSuccessListener {

                    android.widget.Toast.makeText(
                        this,
                        "Ứng tuyển thành công",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()

                }
                .addOnFailureListener {

                    android.widget.Toast.makeText(
                        this,
                        "Ứng tuyển thất bại",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun loadData(company: Company){
        // Job title
        binding.tvJobTitle.text=company.title
        // Company
        binding.tvCompany.text=company.company.name
        // Location
        binding.tvLocation.text=company.location
        // Salary
        binding.tvSalary.text =
            if(company.salaryMin != null && company.salaryMax != null){
                "${company.salaryMin} - ${company.salaryMax}"
            }else{
                "Thương lượng"
            }
        // Job type
        binding.tvJobType.text =
            if(company.types.isNotEmpty())
                company.types[0].nameType
            else
                "Không"

        // Position
        binding.tvPosition.text =
            when(company.experienceLevel){
                "EN" -> "Entry Level"
                "SE" -> "Senior"
                else -> company.experienceLevel
            }

        binding.tvDescription.text =
            HtmlCompat.fromHtml(
                company.description,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        Glide.with(this)
            .load(company.company.image)
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