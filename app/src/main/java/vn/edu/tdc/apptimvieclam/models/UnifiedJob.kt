package vn.edu.tdc.apptimvieclam.models

import java.io.Serializable

data class UnifiedJob(
    val id: String = "",
    val title: String = "",
    val companyName: String = "",
    val location: String = "",
    val salary: String = "",
    val jobType: String = "",
    val description: String = "",
    val logoUrl: String = "",
    val source: String = ""  // "api" hoặc "firebase"
) : Serializable {
    companion object {
        fun fromCompany(company: Company): UnifiedJob {
            val salaryText = when {
                company.salaryMin != null && company.salaryMax != null ->
                    "${company.salaryMin!!.toInt()} - ${company.salaryMax!!.toInt()} USD"
                company.salaryMin != null -> "Từ ${company.salaryMin!!.toInt()} USD"
                else -> "Thỏa thuận"
            }
            return UnifiedJob(
                id = company.title,
                title = company.title,
                companyName = company.company.name,
                location = company.location,
                salary = salaryText,
                jobType = if (company.types.isNotEmpty()) company.types[0].nameType else "",
                description = company.description,
                logoUrl = company.company.image,
                source = "api"
            )
        }

        fun fromJob(job: Job): UnifiedJob {
            return UnifiedJob(
                id = job.id,
                title = job.title,
                companyName = job.companyName,
                location = job.location,
                salary = job.salary,
                jobType = job.jobType,
                description = job.description,
                logoUrl = job.companyLogo,
                source = "firebase"
            )
        }
    }
}