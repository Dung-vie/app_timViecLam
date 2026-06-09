package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.adapters.UnifiedJobAdapter
import vn.edu.tdc.apptimvieclam.databinding.HomeLayoutBinding
import vn.edu.tdc.apptimvieclam.databinding.ItemCompanyLayoutBinding
import vn.edu.tdc.apptimvieclam.databinding.ItemFilterLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import vn.edu.tdc.apptimvieclam.models.CompanyAPI
import vn.edu.tdc.apptimvieclam.models.CompanyList
import vn.edu.tdc.apptimvieclam.models.Job
import vn.edu.tdc.apptimvieclam.models.UnifiedJob

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeLayoutBinding
    private lateinit var adapter: UnifiedJobAdapter
    private lateinit var companyAPI: CompanyAPI

    private val apiJobs = ArrayList<UnifiedJob>()
    private val firebaseJobs = ArrayList<UnifiedJob>()
    private var apiLoaded = false
    private var firebaseLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserName()
        setupRecyclerView()
        loadQuickFilters()
        playMenu()
        loadAddRecruiter()

        // Click vào ô search giả -> sang SearchActivity
        binding.layoutSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // Xem thêm -> sang SearchActivity
        binding.txtMore.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        loadFromApi()
        loadFromFirebase()
    }

    // ─── Setup RecyclerView ───────────────────────────────────────────────────

    private fun setupRecyclerView() {
        adapter = UnifiedJobAdapter(ArrayList()) { job ->
            val intent = Intent(this, JobDetailActivity::class.java)
            intent.putExtra("JOB", job)
            startActivity(intent)
        }
        binding.listJob.layoutManager = LinearLayoutManager(this)
        binding.listJob.adapter = adapter
        binding.listJob.isNestedScrollingEnabled = false
    }

    // ─── Gộp 2 nguồn và hiển thị ─────────────────────────────────────────────

    private fun mergeAndDisplay() {
        if (!apiLoaded || !firebaseLoaded) return
        if (isFinishing || isDestroyed) return

        val merged = ArrayList<UnifiedJob>()
        merged.addAll(firebaseJobs)         // Firebase jobs lên trên
        merged.addAll(apiJobs.take(5))      // Chỉ lấy 5 jobs từ API

        adapter.updateList(merged)

        // Load highlight companies từ API
        if (apiJobs.isNotEmpty()) {
            loadHighlightCompanies()
        }
    }

    // ─── Load từ API ─────────────────────────────────────────────────────────

    private fun loadFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl(CompanyAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        companyAPI = retrofit.create(CompanyAPI::class.java)
        companyAPI.getCompany().enqueue(object : Callback<CompanyList> {
            override fun onResponse(call: Call<CompanyList>, result: Response<CompanyList>) {
                if (!result.isSuccessful) return
                apiJobs.clear()
                result.body()?.companyList?.forEach { company ->
                    apiJobs.add(UnifiedJob.fromCompany(company))
                }
                apiLoaded = true
                mergeAndDisplay()
            }
            override fun onFailure(call: Call<CompanyList>, t: Throwable) {
                Log.e("API_ERROR", t.message.toString())
                apiLoaded = true
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

    // ─── Highlight companies ──────────────────────────────────────────────────

    private fun loadHighlightCompanies() {
        if (isFinishing || isDestroyed) return
        binding.layoutCompany.removeAllViews()

        // Gộp các job theo tên công ty, lấy top 5
        val topCompanies = apiJobs
            .groupBy { it.companyName }
            .values
            .sortedByDescending { it.size }
            .take(5)

        topCompanies.forEach { jobs ->
            val job = jobs.first()
            val itemBinding = ItemCompanyLayoutBinding.inflate(
                layoutInflater, binding.layoutCompany, false
            )
            itemBinding.txtCompany.text = job.companyName
            if (!isFinishing && !isDestroyed) {
                Glide.with(itemBinding.imgLogo)
                    .load(job.logoUrl)
                    .into(itemBinding.imgLogo)
            }
            itemBinding.root.setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("COMPANY", job.companyName)
                startActivity(intent)
            }
            binding.layoutCompany.addView(itemBinding.root)
        }
    }

    // ─── Load username ────────────────────────────────────────────────────────

    private fun loadUserName() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        Firebase.database.reference
            .child("users").child(user.uid).child("name")
            .get()
            .addOnSuccessListener { snapshot ->
                binding.txtName.text = snapshot.getValue(String::class.java) ?: "Người dùng"
            }
            .addOnFailureListener {
                binding.txtName.text = "Người dùng"
            }
    }

    // ─── Quick filters ────────────────────────────────────────────────────────

    private fun loadQuickFilters() {
        val filters = resources.getStringArray(R.array.quick_filters)
        filters.forEach { filter ->
            val itemBinding = ItemFilterLayoutBinding.inflate(
                layoutInflater, binding.layoutQuickFilter, false
            )
            itemBinding.txtFilter.text = filter
            itemBinding.root.setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("FILTER", filter)
                startActivity(intent)
            }
            binding.layoutQuickFilter.addView(itemBinding.root)
        }
    }

    // ─── Menu ─────────────────────────────────────────────────────────────────

    private fun playMenu() {
        binding.bottomMenu.menuHome.setBackgroundResource(R.drawable.bg_menu_selected)
        binding.bottomMenu.menuSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java)); finish()
        }
        binding.bottomMenu.menuSaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java)); finish()
        }
        binding.bottomMenu.menuSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java)); finish()
        }
        binding.bottomMenu.menuAdd.setOnClickListener {
            startActivity(Intent(this, CreateJobActivity::class.java))
        }
        binding.imgNotify.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }

    // ─── Hiển thị nút Add cho Employer/Admin ─────────────────────────────────

    private fun loadAddRecruiter() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance().reference
            .child("users").child(uid).child("role")
            .get()
            .addOnSuccessListener { snapshot ->
                val role = snapshot.getValue(String::class.java)
                binding.bottomMenu.menuAdd.visibility =
                    if (role.equals("EMPLOYER", true) || role.equals("ADMIN", true))
                        View.VISIBLE else View.GONE
            }
    }
}