package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.adapters.JobAdapter
import vn.edu.tdc.apptimvieclam.databinding.SearchLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import vn.edu.tdc.apptimvieclam.models.CompanyAPI
import vn.edu.tdc.apptimvieclam.models.CompanyList

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: SearchLayoutBinding
    private lateinit var companies: ArrayList<Company>
    private lateinit var jobAdapter: JobAdapter
    private lateinit var companyAPI: CompanyAPI
    private lateinit var allCompanies: ArrayList<Company>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomMenu.menuSearch.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuSaved.setOnClickListener {
            val intent = Intent(this, SavedActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

            finish()
        }

        companies = ArrayList()
        allCompanies = ArrayList()
        // tạo adapter trước
        jobAdapter = JobAdapter(companies) { company ->
            val intent = Intent(this, JobDetailActivity::class.java)
            intent.putExtra("JOB", company)
            startActivity(intent)
        }

        binding.rvJobs.layoutManager =
            LinearLayoutManager(this)
        // rồi mới gán vào ListView
        binding.rvJobs.adapter = jobAdapter

        //Test firebase
        val database = Firebase.database
        val myRef = database.getReference("message") //key
        myRef.setValue("Hello, DUNG!") //value
        //ket thuc test

        // SEARCH
        binding.searchJob.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filteredList = allCompanies.filter {
                        it.title.contains(
                            newText ?: "",
                            ignoreCase = true
                        )
                    }
                    jobAdapter.updateList(
                        ArrayList(filteredList)
                    )
                    return true
                }
            }
        )

        //Filter
        binding.spFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val selected =
                        parent?.getItemAtPosition(position)
                            .toString()

                    if (selected == "Tất cả") {
                        jobAdapter.updateList(
                            ArrayList(allCompanies)
                        )

                    } else {
                        val filtered =
                            allCompanies.filter { company ->
                                company.types.any { type ->
                                    type.nameType.equals(
                                        selected,
                                        ignoreCase = true
                                    )
                                }
                            }

                        jobAdapter.updateList(
                            ArrayList(filtered)
                        )
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }


        getCompanies(companies)
    }

    // ham lay filter
    private fun loadFilterFromApi() {
        val filterList = ArrayList<String>()
        filterList.add("Tất cả")
        val typeSet = mutableSetOf<String>()
        allCompanies.forEach { company ->
            company.types.forEach { type ->
                typeSet.add(type.nameType)
            }
        }

        filterList.addAll(typeSet.sorted())

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filterList
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spFilter.adapter = adapter
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

                    Log.d("API_CODE", result.code().toString())

                    if(result.isSuccessful){
                        Log.d("API_SUCCESS", result.body().toString())
                    }
                    //Xư lí nullable
                    companyList?.companyList?.let {
                        companies.clear()
                        companies.addAll(it)
                        allCompanies.clear()
                        allCompanies.addAll(it)
                        loadFilterFromApi()
                    }
                    //Báo cho ListView cập nhật lại dữ liệu
                    jobAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(p0: Call<CompanyList>, p1: Throwable) {
                Log.e("API_ERROR", p1.message.toString())
            }
        })
    }
}