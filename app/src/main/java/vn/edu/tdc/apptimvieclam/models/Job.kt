package vn.edu.tdc.apptimvieclam.models

import java.io.Serializable

data class Job(
    var jobId: String = "",
    var title: String = "",
    var company: String = "",
    var location: String = "",
    var salary: String = "",
    var description: String = ""
) : Serializable