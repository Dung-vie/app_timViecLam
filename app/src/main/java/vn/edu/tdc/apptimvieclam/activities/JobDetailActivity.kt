package vn.edu.tdc.apptimvieclam.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import vn.edu.tdc.apptimvieclam.databinding.JobDetailLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import com.google.firebase.database.FirebaseDatabase

class JobDetailActivity : AppCompatActivity() {

    private lateinit var binding: JobDetailLayoutBinding
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
}