    package vn.edu.tdc.apptimvieclam.models

    import android.accessibilityservice.GestureDescription
    import com.google.gson.annotations.SerializedName
    import java.io.Serializable

    class Company : Serializable {
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
        constructor(
            title: String,
            location: String,
            company: CompanyItem,
            types: ArrayList<Type>,
            publish: String,
            description: String,
            salaryMin: Double?,
            salaryMax: Double?,
            experienceLevel:String
        ) {
            this.title = title
            this.location = location
            this.company = company
            this.types = types
            this.publish = publish
            this.description = description
            this.salaryMax = salaryMax
            this.salaryMin = salaryMin
            this.experienceLevel = experienceLevel
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
        }

    }