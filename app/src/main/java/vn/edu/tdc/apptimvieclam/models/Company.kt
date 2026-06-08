package vn.edu.tdc.apptimvieclam.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Company : Serializable {
   @SerializedName("id") // Hoặc thay bằng đúng tên trường ID mà API của bạn trả về (ví dụ: "job_id")
    var id: String = ""
  
    @SerializedName("title")
    var title: String

    @SerializedName("location")
    var location: String

    @SerializedName("company")
    var company: CompanyItem

    @SerializedName("types")
    var types: ArrayList<Type>

    @SerializedName("published")
    var publish: String

    @SerializedName("description")
    var description: String

    @SerializedName("salary_min")
    var salaryMin: Double? = null

    @SerializedName("salary_max")
    var salaryMax: Double? = null

    @SerializedName("experience_level")
    var experienceLevel: String

    var jobId: String

    var recruiterUid: String

    var status: String = "pending"  // pending -> đang duyệt; approved -> đã phê duyệt
    constructor(
        id: String,
        title: String,
        location: String,
        company: CompanyItem,
        types: ArrayList<Type>,
        publish: String,
        description: String,
        salaryMin: Double?,
        salaryMax: Double?,
        experienceLevel:String,
        jobId: String,
        recruiterUid: String,
        status: String
    ) {
      this.id = id
        this.title = title
        this.location = location
        this.company = company
        this.types = types
        this.publish = publish
        this.description = description
        this.salaryMax = salaryMax
        this.salaryMin = salaryMin
        this.experienceLevel = experienceLevel
        this.jobId = jobId
        this.recruiterUid = recruiterUid
        this.status = status
    }

    constructor() {
      this.id = ""
        this.title = ""
        this.location = ""
        this.company = CompanyItem("", "")
        this.types = ArrayList()
        this.publish = ""
        this.description = ""
        this.salaryMin = null
        this.salaryMax = null
        this.experienceLevel = ""
        this.jobId = ""
        this.recruiterUid = ""
        this.status = ""
    }

    class CompanyItem : Serializable {
        @SerializedName("name")
        var name: String

        @SerializedName("logo")
        var image: String

        constructor(name: String, image: String) {
            this.name = name
            this.image = image
        }
    }

    class Type: Serializable {
        @SerializedName("name")
        var nameType: String

        constructor(nameType: String) {
            this.nameType = nameType
        }
        constructor() {
            this.nameType = ""
        }
    }
}