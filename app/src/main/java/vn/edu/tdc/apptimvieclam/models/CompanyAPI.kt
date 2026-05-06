package vn.edu.tdc.apptimvieclam.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CompanyAPI {
    companion object {
            const val BASE_URL = "https://jobdataapi.com/"
    }

    @GET("api/jobs/?country_code=VN")
    fun getCompany(): Call<CompanyList>

}