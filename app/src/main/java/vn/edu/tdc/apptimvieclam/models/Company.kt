package vn.edu.tdc.apptimvieclam.models

import com.google.gson.annotations.SerializedName

class Company {
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

    constructor(
        title: String,
        location: String,
        company: CompanyItem,
        types: ArrayList<Type>,
        publish: String
    ) {
        this.title = title
        this.location = location
        this.company = company
        this.types = types
        this.publish = publish
    }


    class CompanyItem {
        @SerializedName("name")
        var name: String

        @SerializedName("logo")
        var image: String

        constructor(name: String, image: String) {
            this.name = name
            this.image = image
        }
    }

    class Type {
        @SerializedName("name")
        var nameType: String

        constructor(nameType: String) {
            this.nameType = nameType
        }
    }

}