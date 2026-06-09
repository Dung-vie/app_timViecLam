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
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.adapters.UnifiedJobAdapter
import vn.edu.tdc.apptimvieclam.databinding.SearchLayoutBinding
import vn.edu.tdc.apptimvieclam.models.CompanyAPI
import vn.edu.tdc.apptimvieclam.models.CompanyList
import vn.edu.tdc.apptimvieclam.models.Job
import vn.edu.tdc.apptimvieclam.models.UnifiedJob

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: SearchLayoutBinding
    private lateinit var jobAdapter: UnifiedJobAdapter

    private val allJobs = ArrayList<UnifiedJob>()  // tất cả jobs từ cả 2 nguồn
    private val apiJobs = ArrayList<UnifiedJob>()
    private val firebaseJobs = ArrayList<UnifiedJob>()

    private var apiLoaded = false
    private var firebaseLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playMenu()
        setupRecyclerView()
        setupSearch()

        loadFromApi()
        loadFromFirebase()
    }

    private fun setupRecyclerView() {
        jobAdapter = UnifiedJobAdapter(ArrayList()) { job ->
            val intent = Intent(this, JobDetailActivity::class.java)
            intent.putExtra("JOB", job)
            startActivity(intent)
        }
        binding.rvJobs.layoutManager = LinearLayoutManager(this)
        binding.rvJobs.adapter = jobAdapter
    }

    private fun setupSearch() {
        binding.searchJob.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = allJobs.filter {
                    it.title.contains(newText ?: "", ignoreCase = true) ||
                            it.companyName.contains(newText ?: "", ignoreCase = true)
                }
                jobAdapter.updateList(ArrayList(filtered))
                return true
            }
        })
    }

    // Gộp 2 nguồn và cập nhật UI
    private fun mergeAndDisplay() {
        if (!apiLoaded || !firebaseLoaded) return  // chờ cả 2 load xong
        allJobs.clear()
        allJobs.addAll(firebaseJobs)  // Firebase jobs hiển thị trên
        allJobs.addAll(apiJobs)       // API jobs hiển thị dưới
        jobAdapter.updateList(ArrayList(allJobs))
        setupFilter()
    }

    // ─── Load từ API ─────────────────────────────────────────────────────────

    private fun loadFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl(CompanyAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CompanyAPI::class.java)
        api.getCompany().enqueue(object : Callback<CompanyList> {
            override fun onResponse(call: Call<CompanyList>, result: Response<CompanyList>) {
                if (result.isSuccessful) {
                    apiJobs.clear()
                    result.body()?.companyList?.forEach { company ->
                        apiJobs.add(UnifiedJob.fromCompany(company))
                    }
                    apiLoaded = true
                    mergeAndDisplay()
                }
            }
            override fun onFailure(call: Call<CompanyList>, t: Throwable) {
                Log.e("API_ERROR", t.message.toString())
                apiLoaded = true  // dù lỗi vẫn đánh dấu loaded
                mergeAndDisplay()
            }
        })
    }

    // ─── Load từ Firebase ────────────────────────────────────────────────────

    private fun loadFromFirebase() {
        FirebaseDatabase.getInstance().getReference("jobs")
            .get()
            .addOnSuccessListener { snapshot ->
                firebaseJobs.clear()
                for (child in snapshot.children) {
                    val job = child.getValue(Job::class.java)
                    if (job != null) firebaseJobs.add(UnifiedJob.fromJob(job))
                }
                firebaseLoaded = true
                mergeAndDisplay()
            }
            .addOnFailureListener {
                firebaseLoaded = true
                mergeAndDisplay()
            }
    }

    // ─── Filter ──────────────────────────────────────────────────────────────

    private fun setupFilter() {
        val typeSet = mutableSetOf<String>()
        allJobs.forEach { if (it.jobType.isNotEmpty()) typeSet.add(it.jobType) }

        val filterList = ArrayList<String>()
        filterList.add("Tất cả")
        filterList.addAll(typeSet.sorted())

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFilter.adapter = adapter

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                if (selected == "Tất cả") {
                    jobAdapter.updateList(ArrayList(allJobs))
                } else {
                    jobAdapter.updateList(ArrayList(allJobs.filter {
                        it.jobType.equals(selected, ignoreCase = true)
                    }))
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // ─── Menu ────────────────────────────────────────────────────────────────

    private fun playMenu() {
        binding.bottomMenu.menuSearch.setBackgroundResource(R.drawable.bg_menu_selected)
        binding.bottomMenu.menuHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java)); finish()
        }
        binding.bottomMenu.menuSaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java)); finish()
        }
        binding.bottomMenu.menuSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java)); finish()
        }
    }
}