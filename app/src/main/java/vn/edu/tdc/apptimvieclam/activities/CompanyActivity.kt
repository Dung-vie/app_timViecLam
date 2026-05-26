package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
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

        companies = ArrayList()
        // tạo adapter trước
        adapter = MyListViewAdapter(this, companies)
        // rồi mới gán vào ListView
        binding.listJob.adapter = adapter

        //Test firebase
        val database = Firebase.database
        val myRef = database.getReference("message") //key
        myRef.setValue("Hello, DUNG!") //value
        //ket thuc test

        val spinAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            R.layout.spinner_item_layout
        )

        spinAdapter.setDropDownViewResource(
            R.layout.spinner_item_layout
        )

        binding.spincities.adapter = spinAdapter
        binding.spincities.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    p3: Long
                ) {
                    city = adapterView?.selectedItem.toString()
                    getCompanies(companies)
                }

                override fun onNothingSelected(
                    p0: AdapterView<*>?
                ) {

                }
            }

        binding.listJob.setOnItemClickListener { parent, view, position, id ->

            val company = companies[position]
            val intent = Intent(
                this,
                JobDetailActivity::class.java
            )
            intent.putExtra("JOB", company)
            startActivity(intent)
        }
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