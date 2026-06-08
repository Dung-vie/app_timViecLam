package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.apptimvieclam.databinding.HomeLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import vn.edu.tdc.apptimvieclam.models.CompanyAPI
import vn.edu.tdc.apptimvieclam.models.CompanyList
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.adapters.MyListViewAdapter
import vn.edu.tdc.apptimvieclam.databinding.ItemCompanyLayoutBinding
import vn.edu.tdc.apptimvieclam.databinding.ItemFilterLayoutBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeLayoutBinding
    private var name:String = ""
    private lateinit var companies: ArrayList<Company>
    private lateinit var adapter: MyListViewAdapter
    private lateinit var companyAPI: CompanyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserName()

        companies = ArrayList<Company>()

        adapter = MyListViewAdapter(this, companies)
        binding.listJob.adapter = adapter

        loadQuickFilters()
        playMenu()
        loadAddRecuiter()
        getCompanies(companies)

    }

    private fun playMenu() {
        binding.bottomMenu.menuHome.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
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

        binding.bottomMenu.menuAdd.setOnClickListener {
            startActivity(Intent(this, CreateJobActivity::class.java))
        }

        binding.imgNotify.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)

        }

    }

    private fun loadUserName() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            Firebase.database.reference
                .child("users")
                .child(user.uid)
                .child("name")
                .get()
                .addOnSuccessListener { snapshot ->
                    name = snapshot.getValue(String::class.java)
                        ?: "Người dùng"
                    binding.txtName.text = name
                }
                .addOnFailureListener {
                    binding.txtName.text = "Người dùng"
                }
        }
    }

    private fun loadQuickFilters() {
        val filters = resources.getStringArray(R.array.quick_filters)

        filters.forEach { filter ->
            val itemBinding = ItemFilterLayoutBinding.inflate(
                layoutInflater,
                binding.layoutQuickFilter,
                false
            )

            itemBinding.txtFilter.text = filter

            itemBinding.root.setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("FILTER", filter)

                startActivity(intent)
            }

            binding.layoutQuickFilter.addView(
                itemBinding.root
            )
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
                if (!result.isSuccessful) return

                val companyList = result.body()

                companyList?.companyList?.let {
                    if (isFinishing || isDestroyed) return

                    companies.clear()
                    companies.addAll(it.take(5))
                    adapter.notifyDataSetChanged()

                    loadHighlightCompanies(it)
                }
            }

            override fun onFailure(p0: Call<CompanyList>, p1: Throwable) {
            }
        })
    }

    private fun loadHighlightCompanies(companies: List<Company>) {

        if (isFinishing || isDestroyed) return

        binding.layoutCompany.removeAllViews()

        val topCompanies = companies
            .groupBy { it.company.name }
            .values
            .sortedByDescending { it.size }
            .take(5)

        topCompanies.forEach { jobs ->

            val company = jobs.first()

            val itemBinding = ItemCompanyLayoutBinding.inflate(
                layoutInflater,
                binding.layoutCompany,
                false
            )

            itemBinding.txtCompany.text = company.company.name

            // FIX GLIDE AN TOÀN
            if (!isFinishing && !isDestroyed) {
                Glide.with(itemBinding.imgLogo)
                    .load(company.company.image)
                    .into(itemBinding.imgLogo)
            }

            itemBinding.root.setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("COMPANY", company.company.name)
                startActivity(intent)
            }

            binding.layoutCompany.addView(itemBinding.root)
        }
    }

    private fun loadAddRecuiter() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(uid)
            .child("role")
            .get()
            .addOnSuccessListener { snapshot ->

                val role = snapshot.getValue(String::class.java)

                binding.bottomMenu.menuAdd.visibility =
                    if (role.equals("EMPLOYER", true) ||
                        role.equals("ADMIN", true)) {
                        View.VISIBLE
                    }
                    else {
                        View.GONE
                    }
            }
    }
}