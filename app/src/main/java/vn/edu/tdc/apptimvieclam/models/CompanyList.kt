package vn.edu.tdc.apptimvieclam.models

import com.google.gson.annotations.SerializedName

class CompanyList {
    @SerializedName("results")
    lateinit var companyList:ArrayList<Company>

}