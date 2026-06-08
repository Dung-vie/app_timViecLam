package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.adapters.JobAdapter
import vn.edu.tdc.apptimvieclam.databinding.SearchLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Job

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: SearchLayoutBinding

    private lateinit var jobAdapter: JobAdapter
    private var allJobs: ArrayList<Job> = ArrayList()
    private var jobList: ArrayList<Job> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SearchLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playMenu()
        setupRecyclerView()
        setupSearch()
        setupFilter()
        loadAddRecuiter()

        loadJobs()
    }

    // ================= SETUP =================

    private fun setupRecyclerView() {
        jobAdapter = JobAdapter(jobList,
            onEdit = {},
            onDelete = {},
            onItemClick = { job ->
                val intent = Intent(this, JobDetailActivity::class.java)
                intent.putExtra("JOB", job)
                startActivity(intent)
            }
        )

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

    private fun setupFilter() {
        val filterList = listOf("Tất cả", "Full-time", "Part-time", "Remote")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filterList
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spFilter.adapter = adapter

        binding.spFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val selected = parent?.getItemAtPosition(position).toString()

                    if (selected == "Tất cả") {
                        jobAdapter.updateList(ArrayList(allJobs))
                    } else {
                        val filtered = allJobs.filter {
                            it.jobType.equals(selected, true)
                        }
                        jobAdapter.updateList(ArrayList(filtered))
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    // ================= DATA =================

    private fun loadJobs() {
        val database = FirebaseDatabase.getInstance().reference.child("jobs")

        database.get().addOnSuccessListener { snapshot ->

            val list = ArrayList<Job>()

            for (child in snapshot.children) {
                val job = child.getValue(Job::class.java)
                if (job != null) list.add(job)
            }

            allJobs.clear()
            allJobs.addAll(list)

            jobAdapter.updateList(ArrayList(allJobs))
        }
    }

    // ================= MENU =================

    private fun playMenu() {
        binding.bottomMenu.menuSearch.setBackgroundResource(R.drawable.bg_menu_selected)

        binding.bottomMenu.menuHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.bottomMenu.menuSaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
            finish()
        }

        binding.bottomMenu.menuSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
            finish()
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
                        role.equals("ADMIN", true)
                    ) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
    }
}