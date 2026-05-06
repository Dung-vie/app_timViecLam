package vn.edu.tdc.apptimvieclam.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.adapters.MyListViewAdapter
import vn.edu.tdc.apptimvieclam.databinding.HomeLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import vn.edu.tdc.apptimvieclam.models.CompanyAPI
import vn.edu.tdc.apptimvieclam.models.CompanyList

class CompanyActivity : AppCompatActivity() {
    private lateinit var binding: HomeLayoutBinding
    private lateinit var companies: ArrayList<Company>
    private lateinit var adapter: MyListViewAdapter
    private lateinit var city: String
    private lateinit var companyAPI: CompanyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            R.layout.spinner_item_layout
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_item_layout)

        binding.spincities.onItemSelectedListener = object  : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                //Lấy tên TP dược chọn trong spinner
                city = adapterView?.selectedItem.toString()
//                Log.d("test", city)
                //Cập nhật lại dữ liệu thời tiết
                getCompanies(companies)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        binding.spincities.adapter = spinAdapter
        companies = ArrayList<Company>()
        adapter = MyListViewAdapter(this, companies)
        binding.listJob.adapter = adapter
    }

        //B3:Viết hàm xử lý dữ liệu:
        private fun getCompanies(companies: ArrayList<Company> ) {
            //B1: Xóa dữ liệu cũ
            companies.clear()
            //B2: Định nghĩa đối tượng retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(CompanyAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            //B3: Xây dưnng đối tượng weatherAPI
            companyAPI = retrofit.create(CompanyAPI::class.java)
            //B4: Gọi hàm đọc dữ liệu từ Webservice
            val call = companyAPI.getCompany()
            //B5. Xử lý
            call.enqueue(object : Callback<CompanyList> {
                override fun onResponse(call: Call<CompanyList>, result: Response<CompanyList>) {
                    //Xử lí dữ liệu đọc về tù web Service
                    //Nếu có dữ liệu mới xử lí
                    if (result.isSuccessful) {
                        val companyList = result.body()
                        //Xư lí nullable
                        companyList?.companyList?.let {
                            companies.addAll(it)
                        }
                        //Báo cho ListView cập nhật lại dữ liệu
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(p0: Call<CompanyList>, p1: Throwable) {
                }
            })
        }
}