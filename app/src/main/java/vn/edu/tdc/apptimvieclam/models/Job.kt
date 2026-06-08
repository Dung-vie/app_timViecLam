package vn.edu.tdc.apptimvieclam.models

import java.io.Serializable

data class Job(
    var id: String = "",
    var title: String = "",
    var companyName: String = "",
    var location: String = "",
    var salary: String = "",
    var description: String = "",
    var jobType: String = "",
    var companyLogo: String = "",
    var userId: String = "",
    var createdAt: Long = 0
) : Serializable