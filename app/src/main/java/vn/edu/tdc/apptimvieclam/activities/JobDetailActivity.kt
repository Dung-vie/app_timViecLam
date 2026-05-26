package vn.edu.tdc.apptimvieclam.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import vn.edu.tdc.apptimvieclam.databinding.ActivityJobDetailBinding
import vn.edu.tdc.apptimvieclam.models.Company

class JobDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val company = intent.getSerializableExtra("JOB") as? Company
        if(company != null){
            loadData(company)
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
                "Negotiable"
            }
        // Job type
        binding.tvJobType.text =
            if(company.types.isNotEmpty())
                company.types[0].nameType
            else
                "Unknown"

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